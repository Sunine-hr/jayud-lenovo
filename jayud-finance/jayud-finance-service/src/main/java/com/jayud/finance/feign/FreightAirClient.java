package com.jayud.finance.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 */
@FeignClient(value = "jayud-freight-air-web")
public interface FreightAirClient {

    /**
     * 根据主单集合查询空运订单信息
     */
    @RequestMapping(value = "/api/airfreight/getAirOrderByMainOrderNos")
    public ApiResult getAirOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);
}
