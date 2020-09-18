package com.jayud.airfreight.controller;

import com.jayud.airfreight.annotations.APILog;
import com.jayud.airfreight.service.AirFreightService;
import com.jayud.common.CommonResult;
import com.jayud.airfreight.model.bo.BookingSpaceForm;
import com.jayud.airfreight.service.VivoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReceiveVivoController {
    @Autowired
    AirFreightService airFreightService;

    @ApiOperation(value = "接收")
    @PostMapping("/bookingSpace")
    @APILog
    public CommonResult bookingSpace(@RequestBody @Valid BookingSpaceForm form) {
        //校验数据
        log.info("执行入参校验");

        return CommonResult.success();
    }
}
