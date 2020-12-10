package com.jayud.airfreight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.airfreight.model.po.OrderAddress;
import com.jayud.airfreight.mapper.OrderAddressMapper;
import com.jayud.airfreight.service.IOrderAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 查询地址
     */
    @Override
    public List<OrderAddress> getAddress(List<Long> orderId, Integer businessType) {
        QueryWrapper<OrderAddress> condition = new QueryWrapper<>();
        condition.lambda().in(OrderAddress::getBusinessId, orderId);
        condition.lambda().in(OrderAddress::getBusinessType, businessType);
        return this.baseMapper.selectList(condition);
    }
}
