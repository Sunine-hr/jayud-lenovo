package com.jayud.mall.service;

import com.jayud.mall.model.bo.OrderServiceForm;
import com.jayud.mall.model.po.OrderService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OrderServiceVO;

import java.util.List;

/**
 * <p>
 * 订单服务表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
public interface IOrderServiceService extends IService<OrderService> {

    /**
     * 根据订单id，查询订单服务列表list
     * @param orderId
     * @return
     */
    List<OrderServiceVO> findOrderServiceByOrderId(Long orderId);

    /**
     * 保存订单服务
     * @param form
     */
    void saveOrderService(OrderServiceForm form);

    /**
     * 根据服务id，查询订单服务
     * @param id
     * @return
     */
    OrderServiceVO findOrderServiceById(Long id);
}
