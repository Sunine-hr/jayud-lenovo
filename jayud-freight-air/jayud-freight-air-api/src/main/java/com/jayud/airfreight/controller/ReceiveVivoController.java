package com.jayud.airfreight.controller;

import com.jayud.airfreight.service.AirFreightService;
import com.jayud.common.CommonResult;
import com.jayud.airfreight.model.bo.BookingSpaceForm;
import com.jayud.airfreight.service.VivoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author william
 * @description
 * @Date: 2020-09-16 11:11
 */
@RestController
@Api(tags = "从vivo接收数据")
@RequestMapping("/airfreight/fromVivo")
public class ReceiveVivoController {
    @Autowired
    AirFreightService airFreightService;

    @ApiOperation(value = "向3PL发送订舱信息")
    @PostMapping("/ForwarderService/PostSaveBookingConfirmedData")
    public CommonResult bookingSpace(@RequestBody @Valid BookingSpaceForm form) {
        airFreightService.bookingSpace(form);
        return CommonResult.success();
    }
}
