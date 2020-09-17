package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.ProductBizMapper;
import com.jayud.oms.model.po.ProductBiz;
import com.jayud.oms.service.IProductBizService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductBizServiceImpl extends ServiceImpl<ProductBizMapper, ProductBiz> implements IProductBizService {


    @Override
    public List<ProductBiz> findProductBiz() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status","1");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public ProductBiz getProductBizByCode(String idCode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status","1");//有效的
        queryWrapper.eq("id_code",idCode);
        return baseMapper.selectOne(queryWrapper);
    }

}
