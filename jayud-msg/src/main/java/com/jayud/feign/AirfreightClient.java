package com.jayud.feign;

import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

/**
 * 调用空运模块的服务
 *
 * @author william
 * @description
 * @Date: 2020-09-17 15:27
 */
@FeignClient(value = "jayud-freight-air-api")
public interface AirfreightClient {

    @RequestMapping(value = "/api/airfreight/bookingSpace")
    public Boolean doBookingSpace(@RequestParam(name = "json") String json);


    @RequestMapping(value = "/airfreight/toVivo/forwarder/bookingConfirmed")
    CommonResult forwarderBookingConfirmedFeedback(@RequestBody String value);
}
