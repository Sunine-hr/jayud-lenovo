package com.jayud.tms.service.impl;

import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.mapper.OrderSendCarsMapper;
import com.jayud.tms.model.vo.OrderSendCarsVO;
import com.jayud.tms.service.IOrderSendCarsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
