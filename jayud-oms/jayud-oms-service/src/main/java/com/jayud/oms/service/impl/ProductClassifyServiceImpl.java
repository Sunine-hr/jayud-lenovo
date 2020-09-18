package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.ProductClassifyMapper;
import com.jayud.oms.model.po.ProductClassify;
import com.jayud.oms.service.IProductClassifyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class ProductClassifyServiceImpl extends ServiceImpl<ProductClassifyMapper, ProductClassify> implements IProductClassifyService {

    @Override
    public List<ProductClassify> findProductClassify(Map<String,Object> param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status","1");//有效的
        for(String key : param.keySet()){
            String value = String.valueOf(param.get(key));
            queryWrapper.eq(key,value);
        }
        return baseMapper.selectList(queryWrapper);
    }
}
