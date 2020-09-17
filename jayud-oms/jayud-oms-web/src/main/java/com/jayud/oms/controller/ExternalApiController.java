package com.jayud.oms.controller;

import com.jayud.common.ApiResult;
import com.jayud.model.bo.InputOrderForm;
import com.jayud.oms.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "oms对外接口")
public class ExternalApiController {

    @Autowired
    IOrderInfoService orderInfoService;

    @ApiOperation(value = "保存主订单")
    @RequestMapping(value = "/api/oprMainOrder")
    public ApiResult oprMainOrder(@RequestBody InputOrderForm form) {
        String result = orderInfoService.oprMainOrder(form);
        if(result != null){
            return ApiResult.ok(result);
        }
        return ApiResult.error();
    }

}









    



