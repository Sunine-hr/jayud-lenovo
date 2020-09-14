package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.model.po.ProductBiz;
import com.jayud.oms.mapper.ProductBizMapper;
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
}
