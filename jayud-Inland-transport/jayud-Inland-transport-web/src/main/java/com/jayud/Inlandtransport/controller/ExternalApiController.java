package com.jayud.Inlandtransport.controller;


import com.jayud.Inlandtransport.model.bo.AddOrderInlandTransportForm;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "内陆外部调用")
public class ExternalApiController {

    @Autowired
    private IOrderInlandTransportService orderInlandTransportService;


    @RequestMapping(value = "/api/createOrder")
    public ApiResult<String> createOrder(@RequestBody AddOrderInlandTransportForm form) {
        String orderNo=orderInlandTransportService.createOrder(form);
        return ApiResult.ok(orderNo);
    }
}
