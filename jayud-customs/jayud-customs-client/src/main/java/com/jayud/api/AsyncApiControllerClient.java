package com.jayud.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author william
 * @description
 * @Date: 2020-09-11 10:28
 */
@FeignClient
public interface AsyncApiControllerClient {
    @PostMapping("/api/customs/generalLog/save")
    public void saveLog(@RequestBody Map<String, String> param);
}
