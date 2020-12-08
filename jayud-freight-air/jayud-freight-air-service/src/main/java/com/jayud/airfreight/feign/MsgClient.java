package com.jayud.airfreight.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author william
 * @description
 * @Date: 2020-09-19 10:32
 */
@FeignClient("jayud-msg-config")
public interface MsgClient {

    @RequestMapping(value = "/kafka/producer", method = RequestMethod.POST)
    Map<String,String> consume(@RequestBody Map<String, String> param);

}
