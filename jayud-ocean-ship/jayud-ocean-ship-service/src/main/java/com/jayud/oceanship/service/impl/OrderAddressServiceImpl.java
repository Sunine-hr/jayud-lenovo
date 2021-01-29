package com.jayud.oceanship.service.impl;

import com.jayud.oceanship.model.po.OrderAddress;
import com.jayud.oceanship.mapper.OrderAddressMapper;
import com.jayud.oceanship.service.IOrderAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单地址表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Service
public class OrderAddressServiceImpl extends ServiceImpl<OrderAddressMapper, OrderAddress> implements IOrderAddressService {

}
