package com.jayud.tms.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * tms模块消费oms-mini模块的接口
 */
@FeignClient(value = "jayud-oms-mini-app")
public interface OmsMiniClient {

    /**
     * 小程序司机是否确认接单
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/api/isConfirmJieDan")
    ApiResult<Boolean> isConfirmJieDan(@RequestParam(value = "orderId") Long orderId);

}
