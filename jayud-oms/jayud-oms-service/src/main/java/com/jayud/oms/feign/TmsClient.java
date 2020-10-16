package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.oms.model.bo.InputOrderTransportForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * oms模块消费tms模块的接口
 */
@FeignClient(value = "jayud-tms-web")
public interface TmsClient {

    /**
     * 创建中港订单
     */
    @RequestMapping(value = "/api/createOrderTransport")
    ApiResult<Boolean> createOrderTransport(@RequestBody InputOrderTransportForm form);




}
