package com.jayud.airfreight.service.impl;

import com.jayud.airfreight.model.po.OrderAddress;
import com.jayud.airfreight.mapper.OrderAddressMapper;
import com.jayud.airfreight.service.IOrderAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单地址表 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-30
 */
@Service
public class OrderAddressServiceImpl extends ServiceImpl<OrderAddressMapper, OrderAddress> implements IOrderAddressService {

}
