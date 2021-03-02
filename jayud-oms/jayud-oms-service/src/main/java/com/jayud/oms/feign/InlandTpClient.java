package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.oms.model.bo.InputOrderInlandTransportForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 内陆模块
 */
@FeignClient(value = "jayud-Inland-transport", configuration = FeignRequestInterceptor.class)
public interface InlandTpClient {

    /**
     * 新增/编辑内陆运输单
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/createOrder")
    public ApiResult createOrder(@RequestBody InputOrderInlandTransportForm form);

}
