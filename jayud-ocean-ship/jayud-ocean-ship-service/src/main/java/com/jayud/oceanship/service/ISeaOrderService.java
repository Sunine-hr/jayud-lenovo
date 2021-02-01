package com.jayud.oceanship.service;

import com.jayud.oceanship.bo.AddSeaOrderForm;
import com.jayud.oceanship.po.SeaOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 海运订单表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
public interface ISeaOrderService extends IService<SeaOrder> {

    /**
     * 生成海运单
     * @param addSeaOrderForm
     */
    void createOrder(AddSeaOrderForm addSeaOrderForm);

    /**
     * 生成订单号
     * @return
     */
    String generationOrderNo();

    /**
     * 订单是否存在
     * @param orderNo
     * @return
     */
    boolean isExistOrder(String orderNo);
    /**
     * 根据主订单号获取订单信息
     * @param orderNo
     * @return
     */
    SeaOrder getByMainOrderNO(String orderNo);
}
