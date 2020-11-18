package com.jayud.tms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.mapper.OrderSendCarsMapper;
import com.jayud.tms.model.vo.OrderSendCarsVO;
import com.jayud.tms.service.IOrderSendCarsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单派车信息 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderSendCarsServiceImpl extends ServiceImpl<OrderSendCarsMapper, OrderSendCars> implements IOrderSendCarsService {

    @Override
    public OrderSendCarsVO getOrderSendInfo(String orderNo) {
        return baseMapper.getOrderSendInfo(orderNo);
    }

    @Override
    public int getDriverPendingOrderNum(Long driverId, List<String> orderNos) {
        QueryWrapper<OrderSendCars> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderSendCars::getDriverInfoId, driverId);
        if (CollectionUtils.isNotEmpty(orderNos)) {
            condition.lambda().notIn(OrderSendCars::getOrderNo, orderNos);
        }
        return this.count(condition);
    }

    /**
     * 根据订单编号获取
     */
    @Override
    public OrderSendCars getOrderSendCarsByOrderNo(String orderNo) {
        QueryWrapper<OrderSendCars> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderSendCars::getOrderNo, orderNo);
        return this.baseMapper.selectOne(condition);
    }


}
