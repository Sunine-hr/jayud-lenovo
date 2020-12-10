package com.jayud.airfreight.service;

import com.jayud.airfreight.model.po.OrderAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单地址表 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-30
 */
public interface IOrderAddressService extends IService<OrderAddress> {

    /**
     * 查询地址
     */
    public List<OrderAddress> getAddress(List<Long> orderId, Integer businessType);

}
