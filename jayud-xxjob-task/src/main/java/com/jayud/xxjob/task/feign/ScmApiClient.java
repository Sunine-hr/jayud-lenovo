package com.jayud.xxjob.task.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "jayud-scm-api")
public interface ScmApiClient {

    /**
     * 抓取云报关状态变化
     */
    @RequestMapping(value = "/api/updateHgBill")
    public ApiResult updateHgBill();
}
