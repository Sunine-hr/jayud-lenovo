package com.jayud.oms.service;

import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.oms.model.po.OrderAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

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
     *
     * @param deliveryAddressList
     */
    void addDeliveryAddress(List<OrderDeliveryAddress> deliveryAddressList);

    /**
     * 获取订单提货/送货地址
     *
     * @param orderId
     * @param businessType
     * @return
     */
    List<OrderDeliveryAddress> getDeliveryAddress(List<Long> orderId, Integer businessType);

    void removeByOrderNo(String orderNo, Integer businessType);

    public void removeByBusinessId(Long businessId, Integer businessType);

    /**
     * 获取最新的联系人信息
     */
    public List<OrderAddress> getLastContactInfoByBusinessType(Integer businessType);

    List<OrderAddress> getOrderAddressByBusOrders(List<String> orderNo, Integer businessType);

    void deleteOrderAddressByBusOrders(List<String> orderNo, Integer businessType);


    /**
     * 根据条件查询订单id
     * @param orderAddress
     * @param timeInterval
     * @return
     */
    Set<Long> getOrderAddressOrderIdByTimeInterval(OrderAddress orderAddress, List<String> timeInterval);

    /**
     * 根据提货时间获取子订单号集合
     * @param takeTimeStr
     * @param code
     * @return
     */
    Set<String> getOrderNosByTakeTime(String[] takeTimeStr, Integer code);
}
