package com.jayud.oms.service;

import com.jayud.oms.model.po.OrderTypeNumber;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单单号记录表 服务类
 * </p>
 *
 * @author llj
 * @since 2021-02-26
 */
public interface IOrderTypeNumberService extends IService<OrderTypeNumber> {

    /**
     * 按规则获取订单编号
     * @param preOrderNO
     * @param classCode
     * @return
     */
    String getOrderNo(String preOrderNO, String classCode);
}
