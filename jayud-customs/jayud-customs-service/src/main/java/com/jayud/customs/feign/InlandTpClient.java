package com.jayud.customs.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.customs.model.vo.InputOrderInlandTPVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 内陆模块
 */
@FeignClient(value = "jayud-Inland-transport-web", configuration = FeignRequestInterceptor.class)
public interface InlandTpClient {
    /**
     * 查询内陆订单详情
     */
    @PostMapping(value = "/api/getOrderDetails")
    public ApiResult<InputOrderInlandTPVO> getOrderDetails(@RequestParam("mainOrderNo") String mainOrderNo);
}
