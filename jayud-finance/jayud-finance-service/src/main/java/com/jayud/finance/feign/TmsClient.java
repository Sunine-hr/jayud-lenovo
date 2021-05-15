package com.jayud.finance.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * oms模块消费tms模块的接口
 */
@FeignClient(value = "jayud-tms-web")
public interface TmsClient {


    /**
     * 根据主订单号集合查询中港详情信息
     */
    @RequestMapping(value = "/api/getTmsOrderInfoByMainOrderNos")
    public ApiResult getTmsOrderInfoByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);
}
