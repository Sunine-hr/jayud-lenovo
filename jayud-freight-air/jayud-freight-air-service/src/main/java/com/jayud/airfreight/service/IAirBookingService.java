package com.jayud.airfreight.service;

import com.jayud.airfreight.model.po.AirBooking;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 空运订舱表 服务类
 * </p>
 *
 * @author LDR
 * @since 2020-12-03
 */
public interface IAirBookingService extends IService<AirBooking> {

    /**
     * 添加/修改订舱信息
     */
    void saveOrUpdateAirBooking(AirBooking airBooking);

    /**
     * 根据空运订单id查询订舱信息
     */
    AirBooking getByAirOrderId(Long airOrderId);
}
