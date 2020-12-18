package com.jayud.tms.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@FeignClient(value = "jayud-freight-air-api")
public interface FreightAirApiClient {

    /**
     * vivo派车驳回
     */
    @PostMapping("/airfreight/toVivo/forwarder/forwarderDispatchRejected")
    public ApiResult forwarderDispatchRejected(@RequestParam("dispatchNo") String dispatchNo);

}
