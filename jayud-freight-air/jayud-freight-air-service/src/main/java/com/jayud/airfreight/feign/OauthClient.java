package com.jayud.airfreight.feign;


import com.jayud.common.ApiResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "jayud-oauth-web")
public interface OauthClient {

    /**
     * 根据登录名查询用户信息
     */
    @RequestMapping(value = "/api/getSystemUserByName")
    ApiResult getSystemUserByName(@RequestParam("name") String name);


}
