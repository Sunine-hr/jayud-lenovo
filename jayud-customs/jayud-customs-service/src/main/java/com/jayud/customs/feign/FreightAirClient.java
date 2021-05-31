package com.jayud.customs.feign;


import com.jayud.common.ApiResult;
import com.jayud.customs.model.vo.InputAirOrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@FeignClient(value = "jayud-freight-air-web")
public interface FreightAirClient {
    /**
     * 查询空运订单详情
     */
    @PostMapping(value = "/api/airfreight/getAirOrderDetails")
    public ApiResult<InputAirOrderVO> getAirOrderDetails(@RequestParam("mainOrderNo") String mainOrderNo);
}
