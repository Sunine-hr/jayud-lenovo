package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.OrderReceivableCostMapper;
import com.jayud.oms.model.po.OrderReceivableCost;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单对应应收费用明细 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderReceivableCostServiceImpl extends ServiceImpl<OrderReceivableCostMapper, OrderReceivableCost> implements IService<OrderReceivableCost> {

}
