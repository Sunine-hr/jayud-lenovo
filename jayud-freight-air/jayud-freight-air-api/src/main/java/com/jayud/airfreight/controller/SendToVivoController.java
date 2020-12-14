package com.jayud.airfreight.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.model.bo.vivo.ForwarderAirFreightForm;
import com.jayud.airfreight.model.bo.vivo.ForwarderBookingConfirmedFeedbackForm;
import com.jayud.airfreight.model.bo.vivo.ForwarderLadingFileForm;
import com.jayud.airfreight.model.bo.vivo.ForwarderLadingInfoForm;
import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.service.IAirBookingService;
import com.jayud.airfreight.service.VivoService;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.FileUtil;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public CommonResult forwarderBookingConfirmedFeedback(@RequestBody String value) {
        ForwarderBookingConfirmedFeedbackForm form = JSONUtil.toBean(value, ForwarderBookingConfirmedFeedbackForm.class);
        //参数检验
        CommonResult commonResult = form.checkParam();
        if (commonResult != null) {
            return commonResult;
        }
        Map<String, Object> resultMap = vivoService.forwarderBookingConfirmedFeedback(form);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return CommonResult.success();
        } else {
            return CommonResult.error(MapUtil.getInt(resultMap, "status"),
                    MapUtil.getStr(resultMap, "message"));
        }
    }

//    @ApiOperation(value = "车辆信息传给vivo")
//    @PostMapping("/forwarder/vehicleInfo")
//    public CommonResult forwarderVehicleInfo(@RequestBody @Valid ForwarderVehicleInfoForm form) {
//        if (vivoService.forwarderVehicleInfo(form)) {
//            return CommonResult.success();
//        }
//        return CommonResult.error(ResultEnum.PARAM_ERROR, "调用失败");
//    }

    @ApiOperation(value = "提单文件传给vivo")
    @PostMapping("/forwarder/ladingFile")
    public CommonResult forwarderLadingFile(@RequestBody String value) {
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
            return CommonResult.success();
        } else {
            return CommonResult.error(MapUtil.getInt(resultMap, "status"),
                    MapUtil.getStr(resultMap, "message"));
        }
    }

    @ApiOperation(value = "提单跟踪信息回执给vivo")
    @PostMapping("/forwarder/ladingInfo")
    public CommonResult forwarderLadingInfo(@RequestBody String value) {
        ForwarderLadingInfoForm form = JSONUtil.toBean(value, ForwarderLadingInfoForm.class);
        Map<String, Object> resultMap = vivoService.forwarderLadingInfo(form);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return CommonResult.success();
        } else {
            return CommonResult.error(MapUtil.getInt(resultMap, "status"),
                    MapUtil.getStr(resultMap, "message"));
        }
    }

    @ApiOperation(value = "货代抛空运费用数据到vivo")
    @PostMapping("/forwarder/forwarderAirFarePush")
    public CommonResult forwarderAirFarePush(@RequestBody String value) {

        ForwarderAirFreightForm form = JSONUtil.toBean(value, ForwarderAirFreightForm.class);
        JSONObject jsonObject = new JSONObject(value);
        Long airOrderId = jsonObject.getLong("airOrderId");
        AirBooking airBooking = this.airBookingService.getByAirOrderId(airOrderId);
        form.setBillOfLading(airBooking.getMainNo() + "/" + (StringUtils.isEmpty(airBooking.getSubNo())
                ? "" : airBooking.getSubNo()));

        Map<String, Object> resultMap = vivoService.forwarderAirFarePush(form);
        if (1 == MapUtil.getInt(resultMap, "status")) {
            return CommonResult.success();
        } else {
            return CommonResult.error(MapUtil.getInt(resultMap, "status"),
                    MapUtil.getStr(resultMap, "message"));
        }
    }


}
