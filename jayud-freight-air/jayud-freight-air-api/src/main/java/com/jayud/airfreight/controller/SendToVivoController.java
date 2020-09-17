package com.jayud.airfreight.controller;

import com.jayud.airfreight.service.VivoService;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.airfreight.model.bo.ForwarderBookingConfirmedFeedbackForm;
import com.jayud.airfreight.model.bo.ForwarderLadingFileForm;
import com.jayud.airfreight.model.bo.ForwarderLadingInfoForm;
import com.jayud.airfreight.model.bo.ForwarderVehicleInfoForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @author william
 * @description
 * @Date: 2020-09-14 13:40
 */
@RestController
@Api(tags = "向vivo推送数据")
@RequestMapping("/airfreight/toVivo")
public class SendToVivoController {
    @Autowired
    VivoService vivoService;

    @ApiOperation(value = "确认订舱信息传给vivo")
    @PostMapping("/forwarder/bookingConfirmed")
    public CommonResult forwarderBookingConfirmedFeedback(@RequestBody @Valid ForwarderBookingConfirmedFeedbackForm form) {
        if (vivoService.forwarderBookingConfirmedFeedback(form)) {
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.PARAM_ERROR, "调用失败");
    }

    @ApiOperation(value = "车辆信息传给vivo")
    @PostMapping("/forwarder/vehicleInfo")
    public CommonResult forwarderVehicleInfo(@RequestBody @Valid ForwarderVehicleInfoForm form) {
        if (vivoService.forwarderVehicleInfo(form)) {
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.PARAM_ERROR, "调用失败");
    }

    @ApiOperation(value = "提单文件传给vivo")
    @PostMapping("/forwarder/ladingFile")
    public CommonResult forwarderLadingFile(MultipartFile file, @Valid ForwarderLadingFileForm form) {
        if (vivoService.forwarderLadingFile(form, file)) {
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.PARAM_ERROR, "调用失败");
    }

    @ApiOperation(value = "提单跟踪信息回执给vivo")
    @PostMapping("/forwarder/ladingInfo")
    public CommonResult forwarderLadingInfo(@RequestBody @Valid ForwarderLadingInfoForm form) {
        if (vivoService.forwarderLadingInfo(form)) {
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.PARAM_ERROR, "调用失败");
    }


}
