package com.jayud.airfreight.controller;

import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.model.bo.BookingSpaceForm;
import com.jayud.airfreight.service.AirFreightService;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import io.swagger.annotations.Api;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 被外部模块调用的处理接口
 *
 * @author william
 * @description
 * @Date: 2020-09-17 15:34
 */
@RestController
@Api(tags = "空运被外部调用的接口")
@Slf4j
public class ExternalApiController {
    @Autowired
    AirFreightService airFreightService;

    @RequestMapping(value = "/api/airfreight/bookingSpace")
    public Boolean doBookingSpace(@RequestParam(name = "json") String json) {
        BookingSpaceForm form = getForm(json, BookingSpaceForm.class);
        if (null != form) {
            return airFreightService.bookingSpace(form);
        }
        return false;
    }


    private <T> T getForm(String json, Class<T> clz) {
        return JSONUtil.toBean(json, clz);
    }
}
