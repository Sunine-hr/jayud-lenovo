package com.jayud.airfreight.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.annotations.APILog;
import com.jayud.airfreight.feign.OauthClient;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.service.AirFreightService;
import com.jayud.airfreight.service.VivoService;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.VivoApiResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
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
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private VivoService vivoService;

    @ApiOperation(value = "接收")
    @PostMapping("/bookingSpace")
    @APILog
    public VivoApiResult bookingSpace(@RequestBody @Valid BookingSpaceForm form) {
        //校验子订单参数

        ApiResult result = null;
        switch (form.getModeOfTransport()) {
            case 1:
                InputOrderForm orderForm = new InputOrderForm();
                //查询客户名称
                JSONObject customerInfo = this.vivoService.getCustomerInfoByLoginUserName();
                InputMainOrderForm mainOrderForm = new InputMainOrderForm();
                //主订单设置客户名称
                mainOrderForm.setCustomerName(customerInfo.getStr("name"));
                mainOrderForm.setCustomerCode(customerInfo.getStr("idCode"));
                //TODO 不清楚接单法人和结算单位是否要传
                //组装空运订单
                AddAirOrderForm addAirOrderForm = form.convertAddAirOrderForm();
                orderForm.setOrderForm(mainOrderForm);
                orderForm.setAirOrderForm(addAirOrderForm);
                //暂存订单
                result = this.omsClient.holdOrder(orderForm);
                break;
            default:
                return VivoApiResult.error("暂时只支持空运");
        }
        if (result.getCode() == HttpStatus.SC_OK) {
            return VivoApiResult.error("创建订单失败");
        }
        return VivoApiResult.success();
    }

    @PostMapping("/bookingCancel")
    @ApiOperation(value = "取消订舱单")
    public VivoApiResult bookingCancel(@RequestBody @Valid BookingCancelForm bookingCancelForm) {
        log.info("参数============" + JSONUtil.toJsonStr(bookingCancelForm));
        return VivoApiResult.success();
    }

    @PostMapping("/bookingFile")
    @ApiOperation(value = "货代获取订舱文件")
    public VivoApiResult bookingFile(@RequestParam("transfer_data") String transferData,
                                     @RequestParam("MultipartFile") MultipartFile multipartFile) {

        BookingFileTransferDataForm bookingFileTransferDataForm = new BookingFileTransferDataForm();
        JSONObject jsonObject = JSONUtil.parseObj(transferData);
        bookingFileTransferDataForm.setBookingNo(jsonObject.getStr("Booking_no"));
        bookingFileTransferDataForm.setFileType(jsonObject.getInt("File_type"));
        bookingFileTransferDataForm.setFileSize(jsonObject.getInt("File_size"));
        ValidatorUtil.validate(bookingFileTransferDataForm);
        log.info("参数============{},文件名称==={}", transferData, multipartFile.getOriginalFilename());
        return VivoApiResult.success();
    }

    @PostMapping("/carInfoToForwarder")
    @ApiOperation(value = "抛派车信息到货代")
    public VivoApiResult carInfoToForwarder(@RequestBody @Valid CardInfoToForwarderForm cardInfoToForwarderForm) {

        log.info("参数==============={}", JSONUtil.toJsonStr(cardInfoToForwarderForm));
        return VivoApiResult.success();
    }

    @PostMapping("/carCancel")
    @ApiOperation("vivo取消派车")
    public VivoApiResult carCancel(@RequestBody @Valid CarCancelForm carCancelForm) {
        log.info("参数======{}", JSONUtil.toJsonStr(carCancelForm));
        return VivoApiResult.success();
    }

    @PostMapping("/parameterToForwarder")
    @ApiOperation("vivo抛台账数据到货代")
    public VivoApiResult parameterToForwarder(@RequestBody @Valid ParameterToForwarderForm parameterToForwarderForm) {
        log.info("参数=================={}", JSONUtil.toJsonStr(parameterToForwarderForm));
        return VivoApiResult.success();
    }

}
