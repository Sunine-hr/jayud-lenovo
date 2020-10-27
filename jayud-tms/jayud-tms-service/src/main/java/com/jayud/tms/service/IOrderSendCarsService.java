package com.jayud.tms.service;

import com.jayud.tms.model.po.OrderSendCars;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tms.model.vo.OrderSendCarsVO;

/**
 * <p>
 * 订单派车信息 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderSendCarsService extends IService<OrderSendCars> {

    /**
     * 获取派车及仓库信息
     * @param orderNo
     * @return
     */
    OrderSendCarsVO getOrderSendInfo(String orderNo);

}
