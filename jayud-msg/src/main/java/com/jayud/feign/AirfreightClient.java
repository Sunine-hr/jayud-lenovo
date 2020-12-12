package com.jayud.feign;

import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
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


    /**
     * 提单跟踪信息回执给vivo
     */
    @PostMapping("/airfreight/toVivo/forwarder/ladingInfo")
    CommonResult forwarderLadingInfo(@RequestBody String value);

    /**
     * 提单文件传给vivo
     */
    @PostMapping("/airfreight/toVivo/forwarder/ladingFile")
    public CommonResult forwarderLadingFile(@RequestBody String value);


    /**
     * 货代抛空运费用数据到vivo
     */
    @PostMapping("/forwarder/forwarderAirFarePush")
    public CommonResult forwarderAirFarePush(@RequestBody String value);
}
