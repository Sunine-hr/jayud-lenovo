package com.jayud.xxjob.task.feign;


import com.jayud.common.ApiResult;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "jayud-scm-web")
public interface ScmClient {

    /**
     * 抓取中行汇率
     */
    @RequestMapping(value = "/rateRmbFromBoc/grabExchangeRate")
    public ApiResult grabExchangeRate();

}
