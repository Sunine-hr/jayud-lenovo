package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.po.Goods;
import com.jayud.oms.mapper.GoodsMapper;
import com.jayud.oms.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 货品信息表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2020-12-16
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    /**
     * 根据业务主键id查询商品信息
     */
    @Override
    public List<Goods> getGoodsByBusIds(List<Long> busIds, Integer busType) {
        QueryWrapper<Goods> condition = new QueryWrapper<>();
        condition.lambda().in(Goods::getBusinessId, busIds);
        condition.lambda().eq(Goods::getBusinessType, busType);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public void removeByOrderNo(String orderNo, Integer businessType) {
        QueryWrapper<Goods> condition = new QueryWrapper<>();
        condition.lambda().eq(Goods::getOrderNo, orderNo);
        if (businessType != null) {
            condition.lambda().eq(Goods::getBusinessType, businessType);
        }
        this.baseMapper.delete(condition);
    }

    @Override
    public void removeByBusinessId(Long businessId, Integer businessType) {
        QueryWrapper<Goods> condition = new QueryWrapper<>();
        condition.lambda().eq(Goods::getBusinessId, businessId);
        if (businessType != null) {
            condition.lambda().eq(Goods::getBusinessType, businessType);
        }
        this.baseMapper.delete(condition);
    }

    @Override
    public List<Goods> getGoodsByBusOrders(List<String> orderNo, Integer businessType) {
        QueryWrapper<Goods> condition = new QueryWrapper<>();
        condition.lambda().in(Goods::getOrderNo, orderNo);
        condition.lambda().eq(Goods::getBusinessType, businessType);
        return this.baseMapper.selectList(condition);
    }

}
