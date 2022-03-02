package com.jayud.crm.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * customs模块消费file模块的接口
 */
@FeignClient(value = "jayud-file-web")
public interface FileClient {

    /**
     * 获取根路径
     */
    @RequestMapping(value = "/api/getBaseUrl")
    ApiResult getBaseUrl();


}
