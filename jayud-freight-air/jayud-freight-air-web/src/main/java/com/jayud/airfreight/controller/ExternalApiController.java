package com.jayud.airfreight.controller;

import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.model.bo.AddAirOrderForm;
import com.jayud.airfreight.model.bo.BookingSpaceForm;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.service.AirFreightService;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import io.swagger.annotations.Api;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private IAirOrderService airOrderService;

    @RequestMapping(value = "/api/airfreight/bookingSpace")
    public Boolean doBookingSpace(@RequestParam(name = "json") String json) {
        BookingSpaceForm form = getForm(json, BookingSpaceForm.class);
        if (null != form) {
            return airFreightService.bookingSpace(form);
        }
        return false;
    }

    @RequestMapping(value = "/api/airfreight/createOrder")
    public ApiResult createOrder(@RequestBody AddAirOrderForm addAirOrderForm) {
        airOrderService.createOrder(addAirOrderForm);
        return ApiResult.ok();
    }


    private <T> T getForm(String json, Class<T> clz) {
        return JSONUtil.toBean(json, clz);
    }
}
