package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.OrderService;
import com.jayud.mall.mapper.OrderServiceMapper;
import com.jayud.mall.service.IOrderServiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单服务表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Service
public class OrderServiceServiceImpl extends ServiceImpl<OrderServiceMapper, OrderService> implements IOrderServiceService {

}
