package com.jayud.airfreight.controller;

import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.model.bo.AddAirOrderForm;
import com.jayud.airfreight.model.bo.vivo.BookingSpaceForm;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.model.vo.AirOrderVO;
import com.jayud.airfreight.service.AirFreightService;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.common.ApiResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.entity.InitChangeStatusVO;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.common.enums.ProcessStatusEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 被外部模块调用的处理接口
 *
 * @author william
 * @description
 * @Date: 2020-09-17 15:34
 */
@RestController
@Api(tags = "空运被外部调用的接口")
@Slf4j
public class ExternalApiController {
    @Autowired
    AirFreightService airFreightService;
    @Autowired
    private IAirOrderService airOrderService;

    @RequestMapping(value = "/api/airfreight/bookingSpace")
    public Boolean doBookingSpace(@RequestParam(name = "json") String json) {
        BookingSpaceForm form = getForm(json, BookingSpaceForm.class);
        if (null != form) {
            return airFreightService.bookingSpace(form);
        }
        return false;
    }

    @RequestMapping(value = "/api/airfreight/createOrder")
    public ApiResult createOrder(@RequestBody AddAirOrderForm addAirOrderForm) {
        airOrderService.createOrder(addAirOrderForm);
        return ApiResult.ok();
    }

//    @RequestMapping(value = "订舱驳回 id=空运订单id")
//    @PostMapping(value = "/api/airfreight/bookingRejected")
//    public CommonResult bookingRejected(@RequestBody Map<String, Object> form) {
//        Long airOrderId = MapUtil.getLong(form, "id");
//        if (airOrderId == null) {
//            return CommonResult.error(ResultEnum.PARAM_ERROR);
//        }
//
//        AirOrder airOrder = this.airOrderService.getById(airOrderId);
//        if (!OrderStatusEnum.AIR_A_2.getCode().equals(airOrder.getStatus())) {
//            log.warn("当前订单状态无法进行操作 status={}", OrderStatusEnum.getDesc(airOrder.getStatus()));
//            return CommonResult.error(400, "当前订单状态无法进行操作");
//        }
//        //修改空运订单状态为订舱驳回状态
//        this.airOrderService.bookingRejected(airOrder);
//        return CommonResult.success();
//    }

    @ApiOperation(value = "获取空运订单号")
    @RequestMapping(value = "/api/airfreight/getAirOrderNo")
    public ApiResult getAirOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
        AirOrder airOrder = this.airOrderService.getByMainOrderNo(mainOrderNo);
        if (airOrder != null) {
            initChangeStatusVO.setId(airOrder.getId());
            initChangeStatusVO.setOrderNo(airOrder.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.KY);
            initChangeStatusVO.setOrderTypeDesc(CommonConstant.KY_DESC);
            initChangeStatusVO.setStatus(airOrder.getProcessStatus() + "");
            initChangeStatusVO.setNeedInputCost(airOrder.getNeedInputCost());
            return ApiResult.ok(initChangeStatusVO);
        }
        return ApiResult.error();
    }


    @ApiOperation(value = "关闭空运订单")
    @RequestMapping(value = "/api/airfreight/closeAirOrder")
    public ApiResult closeAirOrder(@RequestBody List<SubOrderCloseOpt> form) {
        for (SubOrderCloseOpt subOrderCloseOpt : form) {
            AirOrder airOrder = new AirOrder();
            airOrder.setProcessStatus(ProcessStatusEnum.CLOSE.getCode());
            airOrder.setNeedInputCost(subOrderCloseOpt.getNeedInputCost());
            airOrder.setUpdateUser(subOrderCloseOpt.getLoginUser());
            airOrder.setUpdateTime(LocalDateTime.now());
            boolean bool = this.airOrderService.updateByOrderNo(subOrderCloseOpt.getOrderNo(), airOrder);
            System.out.println(bool);
        }
        return ApiResult.ok();
    }


    @ApiOperation(value = "查询空运订单详情")
    @PostMapping(value = "/api/airfreight/getAirOrderDetails")
    public ApiResult<AirOrderVO> getAirOrderDetails(@RequestParam("mainOrderNo") String mainOrderNo) {
        AirOrder airOrder = this.airOrderService.getByMainOrderNo(mainOrderNo);
        AirOrderVO airOrderDetails = this.airOrderService.getAirOrderDetails(airOrder.getId());
        return ApiResult.ok(airOrderDetails);
    }


    @ApiModelProperty(value = "查询空运订单信息")
    @RequestMapping(value = "/api/airfreight/getAirOrderInfoByOrderNo")
    public ApiResult<AirOrder> getAirOrderInfoByOrderNo(@RequestParam("airOrderNo") String airOrderNo) {
        AirOrder condition = new AirOrder().setOrderNo(airOrderNo);
        List<AirOrder> airOrders = this.airOrderService.getAirOrderInfo(condition);
        return ApiResult.ok(airOrders.get(0));
    }


    private <T> T getForm(String json, Class<T> clz) {
        return JSONUtil.toBean(json, clz);
    }
}
