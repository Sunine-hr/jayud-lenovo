package com.jayud.airfreight.controller;

import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.model.bo.BookingSpaceForm;
import com.jayud.airfreight.model.bo.ForwarderBookingConfirmedFeedbackForm;
import com.jayud.airfreight.model.bo.ForwarderVehicleInfoForm;
import com.jayud.airfreight.model.vo.FeedbackVO;
import com.jayud.airfreight.service.VivoService;
import com.jayud.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author william
 * @description
 * @Date: 2020-09-14 13:40
 */
@RestController
@Api(tags = "vivo调用3PL数据接口")
@RequestMapping("/airfreight/vivo")
public class VivoController {
    @Autowired
    VivoService vivoService;

    @ApiOperation(value = "向3PL发送订舱信息")
    @PostMapping("/ForwarderService/PostSaveBookingConfirmedData")
    public CommonResult bookingSpace(@RequestBody @Valid BookingSpaceForm form) {
        vivoService.bookingSpace(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "将确认订舱信息传给vivo")
    @PostMapping("/forwarder/bookingConfirmed")
    public String forwarderBookingConfirmedFeedback(@RequestBody @Valid ForwarderBookingConfirmedFeedbackForm form) {
        String errorMessage = vivoService.forwarderBookingConfirmedFeedback(form);
        if (StringUtils.isBlank(errorMessage)) {
            return JSONUtil.toJsonStr(new FeedbackVO(1, "ok"));
        }
        return JSONUtil.toJsonStr(new FeedbackVO(0, errorMessage));
    }


    public String forwarderVehicleInfo(@RequestBody @Valid ForwarderVehicleInfoForm form){
        String errorMessage=vivoService.forwarderVehicleInfo(form);
        if (StringUtils.isBlank(errorMessage)) {
            return JSONUtil.toJsonStr(new FeedbackVO(1, "ok"));
        }
        return JSONUtil.toJsonStr(new FeedbackVO(0, errorMessage));
    }

}
