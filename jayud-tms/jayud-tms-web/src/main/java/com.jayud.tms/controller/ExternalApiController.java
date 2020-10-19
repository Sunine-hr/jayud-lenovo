package com.jayud.tms.controller;

import com.jayud.common.ApiResult;
import com.jayud.tms.model.bo.InputOrderTransportForm;
import com.jayud.tms.model.vo.InputOrderTransportVO;
import com.jayud.tms.service.IOrderTransportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "tms对外接口")
public class ExternalApiController {

    @Autowired
    IOrderTransportService orderTransportService;


    @ApiOperation(value = "创建中港子订单")
    @RequestMapping(value = "/api/createOrderTransport")
    ApiResult createOrderTransport(@RequestBody InputOrderTransportForm form){
        Boolean result = orderTransportService.createOrderTransport(form);
        return ApiResult.ok(result);
    }


    @ApiOperation(value = "获取中港子订单详情")
    @RequestMapping(value = "/api/getOrderTransport")
    ApiResult getOrderTransport(@RequestParam(value = "mainOrderNo") String mainOrderNo){
        InputOrderTransportVO inputOrderTransportVO = orderTransportService.getOrderTransport(mainOrderNo);
        return ApiResult.ok(inputOrderTransportVO);
    }



}









    



