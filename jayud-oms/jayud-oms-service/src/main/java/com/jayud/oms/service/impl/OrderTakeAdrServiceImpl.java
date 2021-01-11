package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.OrderTakeAdrMapper;
import com.jayud.oms.model.po.OrderTakeAdr;
import com.jayud.oms.model.vo.InputOrderTakeAdrVO;
import com.jayud.oms.service.IOrderTakeAdrService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单对应收货地址 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderTakeAdrServiceImpl extends ServiceImpl<OrderTakeAdrMapper, OrderTakeAdr> implements IOrderTakeAdrService {

    @Override
    public List<InputOrderTakeAdrVO> findTakeGoodsInfo(String orderNo) {
        return baseMapper.findTakeGoodsInfo(orderNo);
    }

    /**
     * 获取送货地址数量
     */
    @Override
    public int getDeliveryAddressNum(String orderNo) {
        QueryWrapper<OrderTakeAdr> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderTakeAdr::getOrderNo, orderNo).eq(OrderTakeAdr::getOprType, 2);
        return this.count(condition);
    }

    /**
     * 根据订单编号获取地址
     */
    @Override
    public List<OrderTakeAdr> getOrderTakeAdrByOrderNo(String orderNo, Integer oprType) {
        QueryWrapper<OrderTakeAdr> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderTakeAdr::getOrderNo, orderNo);
        if (oprType != null) {
            condition.lambda().eq(OrderTakeAdr::getOprType, oprType);
        }
        return this.baseMapper.selectList(condition);
    }

}
