package com.jayud.airfreight.controller;

import cn.hutool.json.JSONUtil;
import com.jayud.airfreight.model.bo.AddAirOrderForm;
import com.jayud.airfreight.model.bo.BookingSpaceForm;
import com.jayud.airfreight.service.AirFreightService;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.common.ApiResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    private <T> T getForm(String json, Class<T> clz) {
        return JSONUtil.toBean(json, clz);
    }
}
