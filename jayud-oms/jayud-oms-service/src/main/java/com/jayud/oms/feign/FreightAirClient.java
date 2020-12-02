package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.oms.model.bo.InputAirOrderForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@FeignClient(value = "jayud-freight-air-web")
public interface FreightAirClient {

    /**
     * 创建空运单
     */
    @RequestMapping(value = "/api/airfreight/createOrder")
    ApiResult createOrder(@RequestBody InputAirOrderForm addAirOrderForm);


}
