package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.IntoWarehouseNumber;
import com.jayud.mall.mapper.IntoWarehouseNumberMapper;
import com.jayud.mall.service.IIntoWarehouseNumberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 进仓单号表(取单号) 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-01
 */
@Service
public class IntoWarehouseNumberServiceImpl extends ServiceImpl<IntoWarehouseNumberMapper, IntoWarehouseNumber> implements IIntoWarehouseNumberService {

    @Autowired
    IntoWarehouseNumberMapper intoWarehouseNumberMapper;

    @Override
    public String getWarehouseNo() {
        return intoWarehouseNumberMapper.getWarehouseNo();
    }
}
