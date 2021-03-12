package com.jayud.finance.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.finance.vo.InlandTP.OrderInlandTransportDetails;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 内陆模块
 */
@FeignClient(value = "jayud-Inland-transport-web", configuration = FeignRequestInterceptor.class)
public interface InlandTpClient {

    @ApiOperation(value = "根据主订单号集合查询内陆订单详情")
    @PostMapping(value = "/api/getInlandOrderInfoByMainOrderNos")
    public ApiResult<List<OrderInlandTransportDetails>> getInlandOrderInfoByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);
}
