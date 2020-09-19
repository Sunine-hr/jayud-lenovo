package com.jayud.customs.feign;

import com.jayud.common.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author william
 * @description
 * @Date: 2020-09-19 10:32
 */
@FeignClient("jayud-msg")
public interface MsgClient {
    @PostMapping("/api/msgCenter/send")
    public CommonResult sendMessage(@RequestBody Map<String, String> param);
}
