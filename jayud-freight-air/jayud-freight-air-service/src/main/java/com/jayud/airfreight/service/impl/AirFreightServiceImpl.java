package com.jayud.airfreight.service.impl;

import com.jayud.airfreight.model.bo.vivo.BookingSpaceForm;
import com.jayud.airfreight.service.AirFreightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author william
 * @description
 * @Date: 2020-09-16 18:00
 */
@Service
@Slf4j
public class AirFreightServiceImpl implements AirFreightService {
    @Override
    public Boolean bookingSpace(BookingSpaceForm form) {
        log.info("收到数据");
        return false;
    }
}
