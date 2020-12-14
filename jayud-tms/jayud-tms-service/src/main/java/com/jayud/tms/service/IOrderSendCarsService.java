package com.jayud.tms.service;

import com.jayud.tms.model.po.OrderSendCars;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tms.model.vo.DriverInfoPdfVO;
import com.jayud.tms.model.vo.OrderSendCarsVO;

import java.util.List;

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
     *
     * @param orderNo
     * @return
     */
    OrderSendCarsVO getOrderSendInfo(String orderNo);

    /**
     * 获取司机待接单数量
     */
    public int getDriverPendingOrderNum(Long driverId, List<String> orderNos);

    /**
     * 根据订单编号获取
     */
    public OrderSendCars getOrderSendCarsByOrderNo(String orderNo);

    /**
     * 获取司机资料PDF数据
     * @param orderNo
     * @return
     */
    DriverInfoPdfVO initDriverInfo(String orderNo);

}
