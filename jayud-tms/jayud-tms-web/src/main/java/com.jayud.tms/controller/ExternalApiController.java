package com.jayud.tms.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.tms.model.bo.InputOrderTransportForm;
import com.jayud.tms.model.bo.OprStatusForm;
import com.jayud.tms.model.bo.QueryDriverOrderTransportForm;
import com.jayud.tms.model.bo.TmsChangeStatusForm;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.po.TmsExtensionField;
import com.jayud.tms.model.vo.*;
import com.jayud.tms.service.IOrderSendCarsService;
import com.jayud.tms.service.IOrderTakeAdrService;
import com.jayud.tms.service.IOrderTransportService;
import com.jayud.tms.service.ITmsExtensionFieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@Api(tags = "tms对外接口")
public class ExternalApiController {

    @Autowired
    IOrderTransportService orderTransportService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    private IOrderSendCarsService orderSendCarsService;

    @Autowired
    private IOrderTakeAdrService orderTakeAdrService;
    @Autowired
    private ITmsExtensionFieldService tmsExtensionFieldService;


    @ApiOperation(value = "创建中港子订单")
    @RequestMapping(value = "/api/createOrderTransport")
    ApiResult createOrderTransport(@RequestBody InputOrderTransportForm form) {
        Boolean result = orderTransportService.createOrderTransport(form);
        return ApiResult.ok(result);
    }


    @ApiOperation(value = "获取中港子订单详情")
    @RequestMapping(value = "/api/getOrderTransport")
    ApiResult getOrderTransport(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        InputOrderTransportVO inputOrderTransportVO = orderTransportService.getOrderTransport(mainOrderNo);
        return ApiResult.ok(inputOrderTransportVO);
    }

    @ApiOperation(value = "获取中港订单号")
    @RequestMapping(value = "/api/getTransportOrderNo")
    ApiResult getTransportOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.MAIN_ORDER_NO, mainOrderNo);
        OrderTransport orderTransport = orderTransportService.getOne(queryWrapper);
        if (orderTransport != null) {
            initChangeStatusVO.setId(orderTransport.getId());
            initChangeStatusVO.setOrderNo(orderTransport.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.ZGYS);
            initChangeStatusVO.setStatus(orderTransport.getStatus());
            initChangeStatusVO.setNeedInputCost(orderTransport.getNeedInputCost());
            return ApiResult.ok(initChangeStatusVO);
        }
        return ApiResult.error();
    }

    @ApiOperation(value = "更改中港单状态")
    @RequestMapping(value = "/api/changeCustomsStatus")
    ApiResult changeTransportStatus(@RequestBody List<TmsChangeStatusForm> form) {
        for (TmsChangeStatusForm tms : form) {
            OrderTransport orderTransport = new OrderTransport();
            orderTransport.setStatus(tms.getStatus());
            orderTransport.setNeedInputCost(tms.getNeedInputCost());
            orderTransport.setUpdatedUser(tms.getLoginUser());
            orderTransport.setUpdatedTime(LocalDateTime.now());
            QueryWrapper<OrderTransport> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq(SqlConstant.ORDER_NO, tms.getOrderNo());
            orderTransportService.update(orderTransport, updateWrapper);
        }
        return ApiResult.ok();
    }

    @ApiOperation(value = "根据中港订单号查询中港订单信息")
    @RequestMapping(value = "/api/getTmsOrderByOrderNo")
    public ApiResult<OrderTransport> getTmsOrderByOrderNo(@RequestParam("orderNo") String orderNo) {
        List<OrderTransport> orderTransports = this.orderTransportService
                .getOrderTmsByCondition(new OrderTransport().setOrderNo(orderNo));
        return ApiResult.ok(orderTransports.size() > 0 ? orderTransports.get(0) : orderTransports);
    }


    @ApiOperation(value = "查询司机的中港订单信息")
    @RequestMapping(value = "/api/getDriverOrderTransport")
    public ApiResult getDriverOrderTransport(@RequestBody QueryDriverOrderTransportForm form) {
        //状态转换成中港订单
        form.convertOrderTransStatus();
        List<DriverOrderTransportVO> list = this.orderTransportService.getDriverOrderTransport(form);
        return ApiResult.ok(list);
    }

    @ApiOperation(value = "根据主键查询司机的中港订单信息")
    @RequestMapping(value = "/api/getDriverOrderTransportById")
    public ApiResult getDriverOrderTransportById(@RequestParam("orderId") Long orderId) {
        return ApiResult.ok(this.orderTransportService.getById(orderId));
    }

    @ApiOperation(value = "派车单PDF格式,orderNo=子订单号")
    @RequestMapping(value = "/api/dispatchList")
    public ApiResult dispatchList(@RequestParam(value = "orderNo") String orderNo) {
        SendCarPdfVO sendCarPdfVO = orderTransportService.initPdfData(orderNo, CommonConstant.ZGYS);
        return ApiResult.ok(sendCarPdfVO);
    }


    @ApiOperation(value = "获取司机待接单数量（小程序）")
    @RequestMapping(value = "/api/getDriverPendingOrderNum")
    public ApiResult getDriverOrderTransportDetailById(@RequestParam("driverId") Long driverId
            , @RequestParam("orderNos") List<String> orderNos) {
        return ApiResult.ok(this.orderSendCarsService.getDriverPendingOrderNum(driverId, orderNos));
    }


    @ApiOperation(value = "查询送货地址数量")
    @RequestMapping(value = "/api/getDeliveryAddressNum")
    public ApiResult getDeliveryAddressNum(@RequestParam("orderNo") String orderNo) {
        return ApiResult.ok(this.orderTakeAdrService.getDeliveryAddressNum(orderNo));
    }

    @ApiOperation(value = "获取中港订单状态")
    @RequestMapping(value = "/api/getOrderTransportStatus")
    public ApiResult getOrderTransportStatus(@RequestParam("orderNo") String orderNo) {
        return ApiResult.ok(this.orderTransportService.getOrderTransportStatus(orderNo));
    }


    @ApiOperation(value = "司机反馈状态")
    @RequestMapping(value = "/api/doDriverFeedbackStatus")
    public ApiResult doDriverFeedbackStatus(@RequestBody OprStatusForm form) {
        //货物派送操作，要填补入仓和出仓数据
        if (CommonConstant.CAR_SEND.equals(form.getNextCmd())) {
            this.orderTransportService.driverCustomsClearanceVehicles(form);
        } else {
            this.orderTransportService.doDriverFeedbackStatus(form);
        }

        return ApiResult.ok();
    }


    @ApiOperation(value = "查询提货/收货地址")
    @RequestMapping(value = "/api/getDriverOrderTakeAdrByOrderNo")
    public ApiResult<List<DriverOrderTakeAdrVO>> getDriverOrderTakeAdrByOrderNo(@RequestParam("orderNo") List<String> orderNo
            , @RequestParam("oprType") Integer oprType) {
        List<DriverOrderTakeAdrVO> orderTakeAdrs = this.orderTakeAdrService.getDriverOrderTakeAdr(orderNo, oprType);
        return ApiResult.ok(orderTakeAdrs);
    }

    @ApiOperation(value = "查询派车信息")
    @RequestMapping(value = "/api/getOrderSendCarsByOrderNo")
    public ApiResult<OrderSendCars> getOrderSendCarsByOrderNo(@RequestParam("orderNo") String orderNo) {
        OrderSendCars orderSendCars = this.orderSendCarsService.getOrderSendCarsByOrderNo(orderNo);
        return ApiResult.ok(orderSendCars);
    }

    @ApiOperation(value = "删除派车信息")
    @RequestMapping(value = "/api/deleteDispatchInfoByOrderNo")
    public ApiResult deleteDispatchInfoByOrderNo(@RequestParam("orderNo") String orderNo) {
        this.orderSendCarsService.deleteDispatchInfoByOrderNo(orderNo);
        return ApiResult.ok();
    }

    @ApiModelProperty(value = "保存扩展字段")
    @RequestMapping(value = "/api/saveOrUpdateTmsExtensionField")
    public ApiResult<TmsExtensionField> saveOrUpdateTmsExtensionField(@RequestBody String json) {
        TmsExtensionField tmsExtensionField = JSONUtil.toBean(json, TmsExtensionField.class);
        this.tmsExtensionFieldService.saveOrUpdate(tmsExtensionField);
        return ApiResult.ok();
    }

    @ApiModelProperty(value = "根据第三方订单获取中港订单信息")
    @RequestMapping(value = "/api/getTmsOrderByThirdPartyOrderNo")
    public ApiResult getTmsOrderByThirdPartyOrderNo(@RequestParam("thirdPartyOrderNo") String thirdPartyOrderNo) {
        List<OrderTransport> orderTransports = this.orderTransportService.getOrderTmsByCondition(new OrderTransport().setThirdPartyOrderNo(thirdPartyOrderNo));
        return ApiResult.ok(orderTransports.size() > 0 ? orderTransports.get(0) : null);
    }

    @ApiModelProperty(value = "根据主订单号集合查询中港信息")
    @RequestMapping(value = "/api/getTmsOrderByMainOrderNos")
    public ApiResult getTmsOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos) {
        List<OrderTransport> orderTransports = this.orderTransportService.getTmsOrderByMainOrderNos(mainOrderNos);
        return ApiResult.ok(orderTransports);
    }

}









    



