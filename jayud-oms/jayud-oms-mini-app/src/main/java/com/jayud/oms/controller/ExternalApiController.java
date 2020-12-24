package com.jayud.oms.controller;

import com.jayud.common.ApiResult;
import com.jayud.oms.service.IDriverOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "oms小程序对外接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    IDriverOrderInfoService driverOrderInfoService;



    @ApiOperation(value = "小程序司机是否确认接单")
    @RequestMapping(value = "/api/isConfirmJieDan")
    public ApiResult<Boolean> isConfirmJieDan(@RequestParam(value = "orderId") Long orderId) {
        Boolean result = driverOrderInfoService.isExistOrder(orderId);
        return ApiResult.ok(result);
    }






}









    



