package com.jayud.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hankcs.hanlp.HanLP;
import com.jayud.common.CommonResult;
import com.jayud.tools.mapper.SensitiveCommodityMapper;
import com.jayud.tools.model.bo.QuerySensitiveCommodityForm;
import com.jayud.tools.model.bo.SensitiveCommodityForm;
import com.jayud.tools.model.po.SensitiveCommodity;
import com.jayud.tools.model.vo.SensitiveCommodityVO;
import com.jayud.tools.service.ISensitiveCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 敏感品名表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Service
public class SensitiveCommodityServiceImpl extends ServiceImpl<SensitiveCommodityMapper, SensitiveCommodity> implements ISensitiveCommodityService {

    @Autowired
    SensitiveCommodityMapper sensitiveCommodityMapper;

    @Override
    public List<SensitiveCommodity> getSensitiveCommodityList(QuerySensitiveCommodityForm form) {
//        List<SensitiveCommodity> list = sensitiveCommodityMapper.getSensitiveCommodityList(name);
//        List<SensitiveCommodity> list

        QueryWrapper<SensitiveCommodity> queryWrapper = new QueryWrapper();
        if(form.getName() != null){
            String name = form.getName();
            // 简体转香港繁体
            String s2hkName = HanLP.s2hk(name);
            // 香港繁体转简体
            String hk2sName = HanLP.hk2s(name);
            //忽略简体中文和繁体中文的模糊查询
            queryWrapper.and(wrapper  -> wrapper.like("name", s2hkName).or().like("name", hk2sName));
        }
        List<SensitiveCommodity> list = sensitiveCommodityMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public CommonResult saveSensitiveCommodity(SensitiveCommodityForm sensitiveCommodityForm) {
        String name = sensitiveCommodityForm.getName();
        QueryWrapper<SensitiveCommodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        List<SensitiveCommodity> SensitiveCommodityList = sensitiveCommodityMapper.selectList(queryWrapper);
        if(SensitiveCommodityList.size() > 0){
            return CommonResult.error(-1, "品名已存在,请重新输入");
        }

        SensitiveCommodity sensitiveCommodity = new SensitiveCommodity();
        sensitiveCommodity.setId(sensitiveCommodityForm.getId());
        sensitiveCommodity.setName(sensitiveCommodityForm.getName());
        //hutool 5.4.6 和 4.1.19 版本冲突
//        SensitiveCommodity convert = ConvertUtil.convert(sensitiveCommodity, SensitiveCommodity.class);
        this.saveOrUpdate(sensitiveCommodity);
        return CommonResult.success();
    }

    @Override
    public void deleteSensitiveCommodityById(Long id) {
        sensitiveCommodityMapper.deleteById(id);
    }

    @Override
    public IPage<SensitiveCommodityVO> findSensitiveCommodityByPage(QuerySensitiveCommodityForm form) {
        //定义分页参数
        Page<SensitiveCommodityVO> page = new Page(form.getPageNum(),form.getPageSize());
        IPage<SensitiveCommodityVO> pageInfo = sensitiveCommodityMapper.findSensitiveCommodityByPage(page, form);
        return pageInfo;
    }

    @Override
    public void importExcelV2(List<SensitiveCommodity> list) {

        //导入的list数据，先进行去重,获取去重后的数据集合disposeList
        List<SensitiveCommodity> disposeList = new ArrayList<>();
        Set<SensitiveCommodity> sensitiveCommoditySet = new HashSet<SensitiveCommodity>();
        for(SensitiveCommodity sc : list){
            if(sensitiveCommoditySet.add(sc)){
                disposeList.add(sc);
            };
        }

        //查询数据库的数据
        QueryWrapper<SensitiveCommodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        List<SensitiveCommodity> dbList = sensitiveCommodityMapper.selectList(queryWrapper);

        //需要保存的数据
        List<SensitiveCommodity> saveList = new ArrayList<>();

        //循环对比 导入的Excel数据 和 数据库数据，不存在则新增，存在则跳过
        for(SensitiveCommodity sensitiveCommodity : disposeList){
            String name = sensitiveCommodity.getName();
            //导入的数据不为空，才进行数据对比判断
            if(!"".equals(name)){
                //对比的计数器
                int count = 0;
                for(SensitiveCommodity dbsensitiveCommodity : dbList){
                    String dbName = dbsensitiveCommodity.getName();
                    if(!name.equals(dbName)){
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
                    saveList.add(sensitiveCommodity);
                }
            }
        }
        //保存导入的数据，excel去重，对比数据库数据，最后新增
        this.saveBatch(saveList);

    }
}
