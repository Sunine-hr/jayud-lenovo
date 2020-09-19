package com.jayud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
