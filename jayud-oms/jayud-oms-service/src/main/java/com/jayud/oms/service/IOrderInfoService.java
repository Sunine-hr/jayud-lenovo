package com.jayud.oms.service;

import com.jayud.model.bo.InputOrderForm;
import com.jayud.model.po.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 主订单基础数据表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 操作主订单
     * @param form
     * @return
     */
    public String oprMainOrder(InputOrderForm form);

    /**
     * 订单是否存在
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);


}
