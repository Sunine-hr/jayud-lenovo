package com.jayud.tms.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * oms模块消费customs模块的接口
 */
@FeignClient(value = "jayud-customs-web")
public interface CustomsClient {

    /**
     * 根据主订单集合查询所有报关信息
     */
    @RequestMapping(value = "/api/getCustomsOrderByMainOrderNos")
    ApiResult getCustomsOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);

}
