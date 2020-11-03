package com.jayud.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.tools.mapper.CargoNameReplaceMapper;
import com.jayud.tools.model.bo.CargoNameReplaceForm;
import com.jayud.tools.model.po.CargoNameReplace;
import com.jayud.tools.service.ICargoNameReplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 货物名称替换表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class CargoNameReplaceServiceImpl extends ServiceImpl<CargoNameReplaceMapper, CargoNameReplace> implements ICargoNameReplaceService {

    @Autowired
    CargoNameReplaceMapper cargoNameReplaceMapper;

    @Override
    public List<CargoNameReplace> findCargoNameReplace(CargoNameReplaceForm form) {
        QueryWrapper<CargoNameReplace> queryWrapper = new QueryWrapper<>();
        String hpmc = form.getHpmc();
        String replaceName = form.getReplaceName();
        if(hpmc != null && hpmc != ""){
            queryWrapper.like("hpmc", hpmc);
        }
        if(replaceName != null && replaceName != ""){
            queryWrapper.like("replaceName", replaceName);
        }
        List<CargoNameReplace> list = cargoNameReplaceMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public void importExcel(List<CargoNameReplace> list) {
        //导入的list数据，先进行去重,获取去重后的数据集合disposeList
        List<CargoNameReplace> disposeList = new ArrayList<>();
        Set<String> hpmcSet = new HashSet<String>();
        for(CargoNameReplace cnr : list){
            //判断`货品名称`是否重复,以货品名称为主要判断条件，同一个货品名称只能对应一个替换名称
            if(hpmcSet.add(cnr.getHpmc())){
                disposeList.add(cnr);
            };
        }
        //查询数据库的数据
        QueryWrapper<CargoNameReplace> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("hpmc");
        List<CargoNameReplace> dbList = cargoNameReplaceMapper.selectList(queryWrapper);
        //需要保存的数据
        List<CargoNameReplace> saveList = new ArrayList<>();

        //循环对比 导入的Excel数据 和 数据库数据，不存在则新增，存在则跳过
        for(CargoNameReplace cargoNameReplace : disposeList){
            String hpmc = cargoNameReplace.getHpmc();
            //导入的数据不为空，才进行数据对比判断
            if(!"".equals(hpmc)){
                //对比的计数器
                int count = 0;
                for(CargoNameReplace dbcargoNameReplace1 : dbList){
                    String dbHpmc = dbcargoNameReplace1.getHpmc();
                    if(!hpmc.equals(dbHpmc)){
                        count++;
                    }else{
                        //有相同的数据直接跳出循环,计数器清零
                        count = 0;
                        break;
                    }
                }
                //当数据库数据为空时，或者数据库数据不存在导入的数据
                if(count == dbList.size()){
                    //代表Excel中的所有数据，在数据库中没有找到相同的记录，新增数据
                    saveList.add(cargoNameReplace);
                }
            }
        }
        //保存导入的数据，excel去重，对比数据库数据，最后新增
        this.saveBatch(saveList);
    }

    @Override
    public CommonResult saveCargoNameReplace(CargoNameReplaceForm form) {
        Long id = form.getId();
        String hpmc = form.getHpmc();
        String replaceName = form.getReplaceName();
        QueryWrapper<CargoNameReplace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hpmc", hpmc);
        List<CargoNameReplace> list = cargoNameReplaceMapper.selectList(queryWrapper);
        CargoNameReplace cargoNameReplace = ConvertUtil.convert(form, CargoNameReplace.class);
        if(id != null){
            this.saveOrUpdate(cargoNameReplace);
        }else{
            if(list.size() > 0){
                return CommonResult.error(-1, "`货品名称`已存在,请重新输入");
            }
            this.saveOrUpdate(cargoNameReplace);
        }
        return CommonResult.success("保存`货物名称替换表`成功!");
    }

    @Override
    public CommonResult deleteCargoNameReplace(Long id) {
        cargoNameReplaceMapper.deleteById(id);

        return CommonResult.success("删除`货物名称替换表`，成功！");
    }


}
