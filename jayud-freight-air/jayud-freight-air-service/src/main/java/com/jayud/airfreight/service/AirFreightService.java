package com.jayud.airfreight.service;

import com.jayud.airfreight.model.bo.BookingSpaceForm;

/**
 * 空运货代
 * @author william
 * @description
 * @Date: 2020-09-16 17:59
 */
public interface AirFreightService {
    /**
     * 接收下单订舱信息
     *
     * @param form
     */
    Boolean bookingSpace(BookingSpaceForm form);
}
