package com.jayud.oms.service;

import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.oms.model.po.OrderAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单地址表 服务类
 * </p>
 *
 * @author LDR
 * @since 2020-12-16
 */
public interface IOrderAddressService extends IService<OrderAddress> {

    /**
     * 查询地址
     */
    public List<OrderAddress> getOrderAddressByBusIds(List<Long> orderId, Integer businessType);

    /**
     * 添加/修改订单提货地址
     * @param deliveryAddressList
     */
    void addDeliveryAddress(List<OrderDeliveryAddress> deliveryAddressList);
}
