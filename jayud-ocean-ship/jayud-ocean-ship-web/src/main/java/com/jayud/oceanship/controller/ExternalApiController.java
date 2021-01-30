package com.jayud.oceanship.controller;

import com.jayud.common.ApiResult;
import com.jayud.oceanship.bo.AddSeaOrderForm;
import com.jayud.oceanship.service.ISeaOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 被外部模块调用的处理接口
 *
 * @author
 * @description
 * @Date:
 */
@RestController
@Api(tags = "海运被外部调用的接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    private ISeaOrderService seaOrderService;

    /**
     * 创建海运单
     * @param addSeaOrderForm
     * @return
     */
    @RequestMapping(value = "/api/airfreight/createOrder")
    public ApiResult createOrder(@RequestBody AddSeaOrderForm addSeaOrderForm) {
        seaOrderService.createOrder(addSeaOrderForm);
        return ApiResult.ok();
    }
}
