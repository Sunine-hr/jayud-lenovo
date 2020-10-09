package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * oms模块消费customs模块的接口
 */
@FeignClient(value = "jayud-customs-web")
public interface CustomsClient {

    /**
     * 获取报关单数量
     */
    @RequestMapping(value = "/api/getCustomsOrderNum")
    ApiResult<Integer> getCustomsOrderNum(@RequestParam(value = "mainOrderNo") String mainOrderNo);


}
