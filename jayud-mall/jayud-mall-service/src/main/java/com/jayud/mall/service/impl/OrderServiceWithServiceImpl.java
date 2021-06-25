package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.OrderServiceWith;
import com.jayud.mall.mapper.OrderServiceWithMapper;
import com.jayud.mall.service.IOrderServiceWithService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单服务对应应付费用 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Service
public class OrderServiceWithServiceImpl extends ServiceImpl<OrderServiceWithMapper, OrderServiceWith> implements IOrderServiceWithService {

}
