package com.jayud.airfreight.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.model.bo.vivo.*;
import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.service.IAirBookingService;
import com.jayud.airfreight.service.VivoService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.FileUtil;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
    @Autowired
    private IAirBookingService airBookingService;

    @ApiOperation(value = "确认订舱信息传给vivo")
    @PostMapping("/forwarder/bookingConfirmed")
    public ApiResult forwarderBookingConfirmedFeedback(@RequestBody String value) {
        ForwarderBookingConfirmedFeedbackForm form = JSONUtil.toBean(value, ForwarderBookingConfirmedFeedbackForm.class);
        //参数检验
        ApiResult commonResult = form.checkParam();
        if (commonResult != null) {
            return commonResult;
        }
        Map<String, Object> resultMap = vivoService.forwarderBookingConfirmedFeedback(form);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return ApiResult.ok();
        } else {
            return ApiResult.error(ResultEnum.VIVO_ERROR.getCode(),
                    MapUtil.getStr(resultMap, "message"));
        }
    }

    @ApiOperation(value = "车辆信息传给vivo")
    @PostMapping("/forwarder/vehicleInfo")
    public ApiResult forwarderVehicleInfo(@RequestBody String value) {
        ForwarderVehicleInfoForm form = JSONUtil.toBean(value, ForwarderVehicleInfoForm.class);
        Map<String, Object> resultMap = vivoService.forwarderVehicleInfo(form);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return ApiResult.ok();
        } else {
            return ApiResult.error(ResultEnum.VIVO_ERROR.getCode(),
                    MapUtil.getStr(resultMap, "message"));
        }
    }

    @ApiOperation(value = "提单文件传给vivo")
    @PostMapping("/forwarder/ladingFile")
    public ApiResult forwarderLadingFile(@RequestBody String value) {
        ForwarderLadingFileForm form = JSONUtil.toBean(value, ForwarderLadingFileForm.class);
        //参数检验
//        CommonResult commonResult = form.checkParam();
//        if (commonResult != null) {
//            return commonResult;
//        }

        JSONObject jsonObject = new JSONObject(value);
        String filePath = jsonObject.getStr("filePath");
        String file = jsonObject.getStr("fileName");
        String[] tmp = file.split("\\.");
        String fileType = "";
        if (tmp.length > 1) {
            fileType = tmp[1];
        }
        StringBuilder sb = new StringBuilder().append(form.getId())
                .append("_").append(tmp[0])
                .append("_");
        MultipartFile fileItem = FileUtil.createFileItem(filePath, sb.toString(), true, fileType);
        Map<String, Object> resultMap = vivoService.forwarderLadingFile(form, fileItem);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return ApiResult.ok();
        } else {
            return ApiResult.error(ResultEnum.VIVO_ERROR.getCode(),
                    MapUtil.getStr(resultMap, "message"));
        }
    }

    @ApiOperation(value = "提单跟踪信息回执给vivo")
    @PostMapping("/forwarder/ladingInfo")
    public ApiResult forwarderLadingInfo(@RequestBody String value) {
        ForwarderLadingInfoForm form = JSONUtil.toBean(value, ForwarderLadingInfoForm.class);
        Map<String, Object> resultMap = vivoService.forwarderLadingInfo(form);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return ApiResult.ok();
        } else {
            return ApiResult.error(ResultEnum.VIVO_ERROR.getCode(),
                    MapUtil.getStr(resultMap, "message"));
        }
    }

    @ApiOperation(value = "货代抛空运费用数据到vivo")
    @PostMapping("/forwarder/forwarderAirFarePush")
    public ApiResult forwarderAirFarePush(@RequestBody String value) {

        ForwarderAirFreightForm form = JSONUtil.toBean(value, ForwarderAirFreightForm.class);
        JSONObject jsonObject = new JSONObject(value);
        Long airOrderId = jsonObject.getLong("airOrderId");
        AirBooking airBooking = this.airBookingService.getEnableByAirOrderId(airOrderId);
        form.setBillOfLading(airBooking.getMainNo() + "/" + (StringUtils.isEmpty(airBooking.getSubNo())
                ? "" : airBooking.getSubNo()));

        Map<String, Object> resultMap = vivoService.forwarderAirFarePush(form);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return ApiResult.ok();
        } else {
            return ApiResult.error(ResultEnum.VIVO_ERROR.getCode(),
                    MapUtil.getStr(resultMap, "message"));
        }
    }

    @ApiOperation(value = "货代抛陆运费用数据到vivo")
    @PostMapping("/forwarder/forwarderLandTransportationFarePush")
    public ApiResult forwarderLandTransportationFarePush(@RequestBody String value) {

        ForwarderLandTransportationFareForm form = JSONUtil.toBean(value, ForwarderLandTransportationFareForm.class);

        Map<String, Object> resultMap = vivoService.forwarderLandTransportationFarePush(form);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return ApiResult.ok();
        } else {
            return ApiResult.error(ResultEnum.VIVO_ERROR.getCode(),
                    MapUtil.getStr(resultMap, "message"));
        }
    }

    @PostMapping("/forwarder/forwarderDispatchRejected")
    @ApiOperation("派车驳回")
    public ApiResult forwarderDispatchRejected(@RequestParam("dispatchNo") String dispatchNo) {
        DispatchRejectedForm form = new DispatchRejectedForm();
        form.setDispatchNo(dispatchNo);
        Map<String, Object> resultMap = this.vivoService.forwarderDispatchRejected(form);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return ApiResult.ok();
        } else {
            return ApiResult.error(ResultEnum.VIVO_ERROR.getCode(),
                    MapUtil.getStr(resultMap, "message"));
        }
    }


//    @ApiOperation(value = "订舱驳回关闭")
//    @PostMapping(value = "/forwarderBookingRejectedClosed")
//    public CommonResult forwarderBookingRejectedClosed(@RequestBody @Valid VivoBookingRejectedForm form) {
//        Map<String, Object> resultMap = this.vivoService.forwarderBookingRejected(form.getBookingNo(), form.getStatus());
//        if (1 == MapUtil.getInt(resultMap, "status")) {
//            return CommonResult.success();
//        } else {
//            return CommonResult.error(ResultEnum.VIVO_ERROR,
//                    MapUtil.getStr(resultMap, "message"));
//        }
//    }

}
