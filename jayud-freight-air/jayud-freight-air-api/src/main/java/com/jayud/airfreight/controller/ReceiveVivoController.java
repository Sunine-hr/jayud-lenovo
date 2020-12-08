package com.jayud.airfreight.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.annotations.APILog;
import com.jayud.airfreight.feign.FileClient;
import com.jayud.airfreight.feign.OauthClient;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.service.AirFreightService;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.airfreight.service.VivoService;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.VivoApiResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

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
    @Autowired
    private IAirOrderService airOrderService;
    @Autowired
    private FileClient fileClient;

    @ApiOperation(value = "接收")
    @PostMapping("/bookingSpace")
    @APILog
    public VivoApiResult bookingSpace(@RequestBody @Valid BookingSpaceForm form) {
        //校验子订单参数
        String errorMessage = form.checkTermsType();
        if (errorMessage.length() > 0) {
            return VivoApiResult.error(errorMessage);
        }
        ApiResult result = null;
        switch (form.getModeOfTransport()) {
            case 1:
                result = this.vivoService.createOrder(form);
                break;
            default:
                return VivoApiResult.error("暂时只支持空运");
        }
        if (result.getCode() != HttpStatus.SC_OK) {
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
        //TODO 校验文件完整性

        //判断当前空运订单状态是否是订舱状态
        AirOrder airOrder = this.airOrderService.getByThirdPartyOrderNo(bookingFileTransferDataForm.getBookingNo());
        if (!OrderStatusEnum.AIR_A_2.getCode().equals(airOrder.getStatus())) {
            log.warn("当前订单状态无法进行操作 status={}", OrderStatusEnum.getDesc(airOrder.getStatus()));
            return VivoApiResult.error("当前订单状态无法进行操作");
        }
        //上传文件
        ApiResult result = this.fileClient.uploadFile(multipartFile);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("上传订舱文件失败 message={}", result.getMsg());
            return VivoApiResult.error("上传订舱文件失败");
        }
        FileView fileView = JSONUtil.toBean(result.getData().toString(), FileView.class);
        bookingFileTransferDataForm.setFileView(fileView);
        if (this.vivoService.bookingFile(airOrder, bookingFileTransferDataForm)) {
            log.warn("该booking_no不存在订舱信息 data={}", JSONUtil.toJsonStr(bookingFileTransferDataForm));
            return VivoApiResult.error("该booking_no不存在订舱信息");
        }
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
