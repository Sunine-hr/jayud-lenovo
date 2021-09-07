package com.jayud.xxjob.task.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "jayud-oauth-web")
public interface OauthClient {

    /**
     * 同步渠道手机账号
     */
    @RequestMapping(value = "/api/syncChannelMobileAccount")
    public ApiResult syncChannelMobileAccount();
}
