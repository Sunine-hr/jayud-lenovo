package com.jayud.airfreight.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.annotations.APILog;
import com.jayud.airfreight.feign.FileClient;
import com.jayud.airfreight.feign.OauthClient;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.feign.TmsClient;
import com.jayud.airfreight.model.bo.InputOrderTransportForm;
import com.jayud.airfreight.model.bo.vivo.*;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.service.AirFreightService;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.airfreight.service.VivoService;
import com.jayud.common.ApiResult;
import com.jayud.common.VivoApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.VehicleTypeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileUtil;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;
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
    @Autowired
    private TmsClient tmsClient;


    @ApiOperation(value = "vivo抛订舱数据到货代")
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
                result = this.vivoService.createAirOrder(form);
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
    @APILog
    public VivoApiResult bookingCancel(@RequestBody @Valid BookingCancelForm bookingCancelForm) {
        //查询空运订单
        AirOrder airOrder = this.airOrderService.getByThirdPartyOrderNo(bookingCancelForm.getBookingNo());
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(airOrder.getProcessStatus())) {
            log.error("当前状态无法取消订舱 processStatus={}", ProcessStatusEnum.getDesc(airOrder.getProcessStatus()));
            return VivoApiResult.error("当前状态无法取消订舱");
        }
        //获取主订单号
        //根据主订单号设置状态
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", airOrder.getMainOrderNo());
        map.put("status", OrderStatusEnum.MAIN_6.getCode());
        this.omsClient.updateByMainOrderNo(JSONUtil.toJsonStr(map));
        return VivoApiResult.success();
    }

    @PostMapping("/bookingFile")
    @ApiOperation(value = "货代获取订舱文件")
    @APILog
    public VivoApiResult bookingFile(@RequestParam("transfer_data") String transferData,
                                     @RequestParam("MultipartFile") MultipartFile multipartFile) {

        BookingFileTransferDataForm bookingFileTransferDataForm = new BookingFileTransferDataForm();
        JSONObject jsonObject = JSONUtil.parseObj(transferData);
        bookingFileTransferDataForm.setBookingNo(jsonObject.getStr("Booking_no"));
        bookingFileTransferDataForm.setFileType(jsonObject.getInt("File_type"));
        bookingFileTransferDataForm.setFileSize(jsonObject.getInt("File_size"));
        ValidatorUtil.validate(bookingFileTransferDataForm);
        if (multipartFile == null) {
            log.warn("订舱文件不能为空");
            return VivoApiResult.error("订舱文件不能为空");
        }

        //TODO 校验文件完整性

        //判断当前空运订单状态是否是订舱状态
        AirOrder airOrder = this.airOrderService.getByThirdPartyOrderNo(bookingFileTransferDataForm.getBookingNo());
        if (!OrderStatusEnum.AIR_A_2.getCode().equals(airOrder.getStatus())) {
            log.warn("当前订单状态无法进行操作 status={}", OrderStatusEnum.getDesc(airOrder.getStatus()));
            return VivoApiResult.error("当前订单状态无法进行操作");
        }

        File file = null;
        try {
            file = FileUtil.multipartFileToFile(multipartFile);
        } catch (Exception e) {
            log.error("文件流转换失败 message=" + e.getMessage(), e);
            throw new JayudBizException("文件流转换失败");
        }
        //http://127.0.0.1:8207/file/uploadFile
        //上传文件
        //TODO url配置到配置中心
        String feedback = HttpRequest.post("http://test.oms.jayud.com:9448/jayudFile/file/uploadFile")
                .header("token", "3118872f-cb0b-4345-9a7f-4bdc19c97bbd")
                .contentType("multipart/form-data")
                .form("file", file)
                .execute()
                .body();

        ApiResult result = JSONUtil.toBean(feedback, ApiResult.class);
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


    @ApiOperation(value = "订舱驳回")
    @PostMapping(value = "/bookingRejected")
    @APILog
    public VivoApiResult bookingRejected(@RequestBody @Valid VivoBookingRejectedForm form) {
        //判断当前空运订单状态是否是订舱状态
        AirOrder airOrder = this.airOrderService.getByThirdPartyOrderNo(form.getBookingNo());
        if (airOrder == null) {
            log.warn("当前订单不存在 bookingNo={}", form.getBookingNo());
            return VivoApiResult.error("当前订单不存在");
        }
        if (!OrderStatusEnum.AIR_A_2.getCode().equals(airOrder.getStatus())) {
            log.warn("当前订单状态无法进行操作 status={}", OrderStatusEnum.getDesc(airOrder.getStatus()));
            return VivoApiResult.error("当前订单状态无法进行操作");
        }
        //修改空运订单状态为订舱驳回状态
        this.airOrderService.bookingRejected(airOrder);
        return VivoApiResult.success();
    }

    @PostMapping("/carInfoToForwarder")
    @ApiOperation(value = "抛派车信息到货代")
    @APILog
    public VivoApiResult carInfoToForwarder(@RequestBody @Valid CardInfoToForwarderForm form) {
        //车型
        if (VehicleTypeEnum.getCode(form.getDemandTruckType()) == null) {
            log.warn("无法配对车型 size={}", form.getDemandTruckType());
            return VivoApiResult.error("无法配对车型");
        }

        //获取海关代码
        ApiResult result = this.omsClient.getPortCodeByName(form.getExportCustomsPort());
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("根据海关名称查询海关代码接口请求失败 message={}", result.getMsg());
            return VivoApiResult.error("创建订单失败");
        }
        if (result.getData() == null) {
            log.warn("找不到对应的出口口岸 message={}", result.getMsg());
            return VivoApiResult.error("找不到对应的出口口岸");
        }
        //设置海关
        form.setExportCustomsPort(String.valueOf(result.getData()));
        //组装订单
        InputOrderTransportForm orderTransportForm = form.assemblyTmsObj();
        //根据booking_no查询货品信息

        result = this.vivoService.createTmsOrder(form, orderTransportForm);
        if (result.getCode() != HttpStatus.SC_OK) {
            return VivoApiResult.error("创建订单失败");
        }
        return VivoApiResult.success();
    }

    @PostMapping("/carCancel")
    @ApiOperation("vivo取消派车")
    @APILog
    public VivoApiResult carCancel(@RequestBody @Valid CarCancelForm carCancelForm) {
        //查询中港订单
        ApiResult result = this.tmsClient.getTmsOrderByThirdPartyOrderNo(carCancelForm.getDispatchNo());
        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("查询中港订单请求失败");
            return VivoApiResult.error("取消派车失败");
        }

        if (result.getData() == null) {
            log.error("不存在派车信息 派车号={}", carCancelForm.getDispatchNo());
            return VivoApiResult.error("不存在派车信息");
        }
        JSONObject data = new JSONObject(result.getData());

        //根据主订单号设置状态
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", data.getStr("mainOrderNo"));
        map.put("status", OrderStatusEnum.MAIN_6.getCode());
        this.omsClient.updateByMainOrderNo(JSONUtil.toJsonStr(map));
        return VivoApiResult.success();
    }


    @PostMapping("/dispatchRejected")
    @ApiOperation("vivo派车驳回")
    @APILog
    public VivoApiResult dispatchRejected(@RequestBody @Valid CarCancelForm carCancelForm) {
        //查询中港订单
        ApiResult result = this.tmsClient.getTmsOrderByThirdPartyOrderNo(carCancelForm.getDispatchNo());
        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("查询中港订单请求失败");
            return VivoApiResult.error("取消派车失败");
        }

        if (result.getData() == null) {
            log.error("不存在派车信息 派车号={}", carCancelForm.getDispatchNo());
            return VivoApiResult.error("不存在派车信息");
        }
        JSONObject data = new JSONObject(result.getData());
        String tmsOrderNo = data.getStr("orderNo");
        Long id = data.getLong("id");
        String orderStatus = data.getStr("status").split("_")[1];
        String status = OrderStatusEnum.TMS_T_5.getCode().split("_")[1];
        //查询订单状态是不是在提仓之前进行驳回
        if (Integer.parseInt(status) <= Integer.parseInt(orderStatus)) {
            log.warn("车辆已提货,无法进行派车驳回操作 tmsOrder={} status={}", tmsOrderNo, data.getStr("status"));
            return VivoApiResult.error("车辆已提货,无法进行派车驳回操作");
        }
        //删除派车信息
        this.tmsClient.deleteDispatchInfoByOrderNo(tmsOrderNo);
        //物流轨迹记录删除
        this.omsClient.deleteLogisticsTrackByType(id, BusinessTypeEnum.ZGYS.getCode());

        //根据主订单号设置状态
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", data.getStr("mainOrderNo"));
        map.put("status", OrderStatusEnum.MAIN_7.getCode());
        this.omsClient.updateByMainOrderNo(JSONUtil.toJsonStr(map));
        return VivoApiResult.success();
    }

    @PostMapping("/parameterToForwarder")
    @ApiOperation("vivo抛台账数据到货代")
    @APILog
    public VivoApiResult parameterToForwarder(@RequestBody @Valid ParameterToForwarderForm parameterToForwarderForm) {
        log.info("参数=================={}", JSONUtil.toJsonStr(parameterToForwarderForm));
        return VivoApiResult.success();
    }


}
