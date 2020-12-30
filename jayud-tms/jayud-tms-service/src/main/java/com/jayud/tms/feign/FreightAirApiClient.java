package com.jayud.tms.feign;


import com.jayud.common.ApiResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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


    /**
     * 车辆信息传给vivo
     */
    @PostMapping("/airfreight/toVivo/forwarder/vehicleInfo")
    public ApiResult forwarderVehicleInfo(@RequestBody String value);

}
