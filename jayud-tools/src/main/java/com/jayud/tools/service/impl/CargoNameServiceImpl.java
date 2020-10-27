package com.jayud.tools.service.impl;

import com.jayud.tools.model.po.CargoName;
import com.jayud.tools.mapper.CargoNameMapper;
import com.jayud.tools.service.ICargoNameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void importExcel(List<CargoName> list) {
//        cargoNameMapper.importExcel(excels);

        //mybatis-plus插入
        list.forEach(itme -> {
            CargoName cargoName = itme;
            cargoName.insert();
        });

    }
}
