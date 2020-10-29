package com.jayud.tools.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.tools.mapper.CargoNameMapper;
import com.jayud.tools.model.po.CargoName;
import com.jayud.tools.model.vo.CargoNameSmallVO;
import com.jayud.tools.model.vo.CargoNameVO;
import com.jayud.tools.service.ICargoNameService;
import com.jayud.tools.utils.MathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 货物名称表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Service
public class CargoNameServiceImpl extends ServiceImpl<CargoNameMapper, CargoName> implements ICargoNameService {

    @Autowired
    CargoNameMapper cargoNameMapper;

    @Override
    public void importExcel(List<List<Object>> list) {
        //构造插入的数据
        List<CargoName> cargoNameList = new ArrayList<>();
        //int i = 2，从第三行记录开是计算，跳过表头列
        for(int i=2; i<list.size(); i++){
            List<Object> o = list.get(i);
            CargoName cargoName = new CargoName();
            cargoName.setXh(Long.valueOf(String.valueOf(o.get(0))));
            cargoName.setDh(String.valueOf(o.get(1)));
            cargoName.setDz(MathUtils.getBigDecimal(o.get(2)));
            cargoName.setYtdh(String.valueOf(o.get(3)));
            cargoName.setTdh(String.valueOf(o.get(4)));
            cargoName.setSl(Integer.valueOf(String.valueOf(o.get(5))));
            cargoName.setZl(MathUtils.getBigDecimal(o.get(6)));
            cargoName.setRemark(String.valueOf(o.get(7)));
            cargoName.setHpmc(String.valueOf(o.get(8)));
            cargoName.setJs(Integer.valueOf(String.valueOf(o.get(9))));
            cargoName.setPce(String.valueOf(o.get(10)));
            //o.get(11) 空列
            cargoName.setJz(MathUtils.getBigDecimal(o.get(12)));
            cargoName.setXm1(String.valueOf(o.get(13)));
            //o.get(14) 空列
            cargoName.setXm2(String.valueOf(o.get(15)));
            cargoName.setAddress(String.valueOf(o.get(16)));
            cargoName.setHm1(String.valueOf(o.get(17)));
            //o.get(18) 空列
            cargoName.setXm3(String.valueOf(o.get(19)));
            cargoName.setHm2(String.valueOf(o.get(20)));
            cargoName.setBjdh(String.valueOf(o.get(21)));

            //add cargoName
            cargoNameList.add(cargoName);

        }

        this.saveBatch(cargoNameList);
    }

    @Override
    public List<CargoNameVO> findCargoNameListByA() {
        return cargoNameMapper.findCargoNameListByA();
    }

    @Override
    public List<CargoNameVO> findCargoNameListByB() {
        return cargoNameMapper.findCargoNameListByB();
    }

    @Override
    public void deleteAllCargoName() {
        cargoNameMapper.deleteAllCargoName();
    }

    @Override
    public void importExcelV2(List<CargoName> list) {
        //导入前，先清空表里面的数据
        cargoNameMapper.truncateCargoName();
        //批量插入数据
        this.saveBatch(list);
    }

    @Override
    public List<CargoNameSmallVO> findCargoNameListByAV2() {
        return cargoNameMapper.findCargoNameListByAV2();
    }

    @Override
    public List<CargoNameSmallVO> findCargoNameListByBV2() {
        return cargoNameMapper.findCargoNameListByBV2();
    }

    @Override
    public void truncateCargoName() {
        cargoNameMapper.truncateCargoName();
    }
}
