package com.jayud.airfreight.service;

import com.jayud.airfreight.model.bo.BookingSpaceForm;
import com.jayud.airfreight.model.bo.ForwarderBookingConfirmedFeedbackForm;
import com.jayud.airfreight.model.bo.ForwarderVehicleInfoForm;

/**
 * vivo数据接口服务
 * @author william
 * @description
 * @Date: 2020-09-14 13:47
 */
public interface VivoService {
    /**
     * 下单订舱
     *
     * @param form
     */
    void bookingSpace(BookingSpaceForm form);

    /**
     * 货代确认订舱
     *
     * @param form
     * @return
     */
    String forwarderBookingConfirmedFeedback(ForwarderBookingConfirmedFeedbackForm form);


    /**
     * 货代车辆信息
     *
     * @param form
     * @return
     */
    String forwarderVehicleInfo(ForwarderVehicleInfoForm form);
}
