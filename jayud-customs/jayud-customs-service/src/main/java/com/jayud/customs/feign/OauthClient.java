package com.jayud.customs.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jayud-oauth-web")
public interface OauthClient {

    /**
     * 根据用户名获取用户所属法人主体
     */
    @RequestMapping(value = "/api/getLegalIdBySystemName")
    public ApiResult getLegalIdBySystemName(@RequestParam("loginName") String loginName) ;
}
