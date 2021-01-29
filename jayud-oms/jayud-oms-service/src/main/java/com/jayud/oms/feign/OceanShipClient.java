package com.jayud.oms.feign;

import com.jayud.common.ApiResult;
import com.jayud.oms.model.bo.InputSeaOrderForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * oms模块消费sea模块的接口
 */
@FeignClient("jayud-ocean-ship-web")
public interface OceanShipClient {

    /**
     * 创建空运单
     */
    @RequestMapping(value = "/api/oceanship/createOrder")
    ApiResult createOrder(@RequestBody InputSeaOrderForm inputSeaOrderForm);
}
