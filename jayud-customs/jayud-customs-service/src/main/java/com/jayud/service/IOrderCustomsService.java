package com.jayud.service;

import com.jayud.model.bo.InputOrderCustomsForm;
import com.jayud.model.po.OrderCustoms;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 报关业务订单表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderCustomsService extends IService<OrderCustoms> {

    /**
     * 报关子订单是否存在
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);

    /**
     *
     * @param form
     * @return
     */
    public boolean oprOrderCustoms(InputOrderCustomsForm form);

}
