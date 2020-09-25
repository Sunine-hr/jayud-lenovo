package com.jayud.airfreight.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.service.AirFreightService;
import com.jayud.common.CommonResult;
import com.jayud.common.VivoApiResult;
import com.jayud.common.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
//    @APILog
    public VivoApiResult bookingSpace(@RequestBody @Valid BookingSpaceForm form) {
        //校验数据
        log.info("执行入参校验");
        return VivoApiResult.success();
    }

    @PostMapping("/bookingCancel")
    @ApiOperation(value = "取消订舱单")
    public VivoApiResult bookingCancel(@RequestBody @Valid BookingCancelForm bookingCancelForm){
        log.info("参数============"+ JSONUtil.toJsonStr(bookingCancelForm));
        return VivoApiResult.success();
    }

    @PostMapping("/bookingFile")
    @ApiOperation(value = "货代获取订舱文件")
    public VivoApiResult bookingFile(@RequestParam("transfer_data") String transferData,
                                     @RequestParam("MultipartFile") MultipartFile multipartFile){

        BookingFileTransferDataForm bookingFileTransferDataForm  = new BookingFileTransferDataForm();
        JSONObject jsonObject = JSONUtil.parseObj(transferData);
        bookingFileTransferDataForm.setBookingNo(jsonObject.getStr("Booking_no"));
        bookingFileTransferDataForm.setFileType(jsonObject.getInt("File_type"));
        bookingFileTransferDataForm.setFileSize(jsonObject.getInt("File_size"));
        ValidatorUtil.validate(bookingFileTransferDataForm);
        log.info("参数============{},文件名称==={}",transferData,multipartFile.getOriginalFilename());
        return VivoApiResult.success();
    }

    @PostMapping("/carInfoToForwarder")
    @ApiOperation(value = "抛派车信息到货代")
    public VivoApiResult carInfoToForwarder(@RequestBody @Valid CardInfoToForwarderForm cardInfoToForwarderForm){

        log.info("参数==============={}",JSONUtil.toJsonStr(cardInfoToForwarderForm));
        return VivoApiResult.success();
    }

    @PostMapping("/carCancel")
    @ApiOperation("vivo取消派车")
    public VivoApiResult carCancel(@RequestBody @Valid CarCancelForm carCancelForm){
        log.info("参数======{}",JSONUtil.toJsonStr(carCancelForm));
        return VivoApiResult.success();
    }

    @PostMapping("/parameterToForwarder")
    @ApiOperation("vivo抛台账数据到货代")
    public VivoApiResult parameterToForwarder(@RequestBody @Valid ParameterToForwarderForm parameterToForwarderForm){
        log.info("参数=================={}",JSONUtil.toJsonStr(parameterToForwarderForm));
        return VivoApiResult.success();
    }

}
