package com.jayud.tms.service;

import com.jayud.tms.model.bo.InputOrderTransportForm;
import com.jayud.tms.model.po.OrderTransport;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 中港运输订单 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderTransportService extends IService<OrderTransport> {


    /**
     * 创建订单
     * @param form
     * @return
     */
    boolean createOrderTransport(InputOrderTransportForm form);

    /**
     * 订单是否存在
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);


}
