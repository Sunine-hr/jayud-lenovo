package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.ProductClassifyMapper;
import com.jayud.oms.model.enums.StatusEnum;
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
    public List<ProductClassify> findProductClassify(Map<String, Object> param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", "1");//有效的
        for (String key : param.keySet()) {
            String value = String.valueOf(param.get(key));
            queryWrapper.eq(key, value);
        }
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询所有启用的父类产品分类
     */
    @Override
    public List<ProductClassify> getEnableParentProductClassify(String status) {
        QueryWrapper<ProductClassify> condition = new QueryWrapper<>();
        condition.lambda().eq(ProductClassify::getStatus, status)
                .eq(ProductClassify::getFId, 0);

        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据id集合查询所有产品分类
     */
    @Override
    public List<ProductClassify> getByIds(List<Long> ids) {
        return this.baseMapper.selectBatchIds(ids);
    }


    public ProductClassify getProductClassifyId(String str) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", str);
        queryWrapper.eq("f_id", 0);
        ProductClassify productClassify = baseMapper.selectOne(queryWrapper);
        return productClassify;
    }

    /**
     * 根据产品编码集合查询产品
     */
    @Override
    public List<ProductClassify> getIdCodes(List<String> idCodes) {
        QueryWrapper<ProductClassify> condition = new QueryWrapper<>();
        condition.lambda().in(ProductClassify::getIdCode, idCodes);
        return this.baseMapper.selectList(condition);
    }


}
