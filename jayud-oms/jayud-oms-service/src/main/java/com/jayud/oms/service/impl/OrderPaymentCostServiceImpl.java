package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.OrderPaymentCostMapper;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.service.IOrderPaymentCostService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单对应应付费用明细 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderPaymentCostServiceImpl extends ServiceImpl<OrderPaymentCostMapper, OrderPaymentCost> implements IOrderPaymentCostService {

}