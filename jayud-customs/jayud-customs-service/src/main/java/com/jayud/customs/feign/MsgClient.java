package com.jayud.customs.feign;

import com.jayud.common.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
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
    public void consume(@RequestBody Map<String,String> param);
}
