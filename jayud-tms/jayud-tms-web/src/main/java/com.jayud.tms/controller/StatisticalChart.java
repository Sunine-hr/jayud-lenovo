package com.jayud.tms.controller;

import com.jayud.common.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "中港统计图")
public class StatisticalChart {

    @ApiModelProperty(value = "订单数扇形统计图")
    @RequestMapping(value = "/api/getOrderNumSectorChart")
    public ApiResult<Map<String, Object>> getOrderNumSectorChart() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("name", "中港");

        return ApiResult.ok();
    }
}
