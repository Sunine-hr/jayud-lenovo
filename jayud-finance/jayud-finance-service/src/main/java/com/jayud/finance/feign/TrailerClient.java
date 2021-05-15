package com.jayud.finance.feign;

import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * oms模块消费trailer模块的接口
 */
@FeignClient(value = "jayud-trailer-web")
public interface TrailerClient {

    /**
     * 根据主订单号查询所有详情
     *
     * @param mainOrderNos
     * @return
     */
    @RequestMapping(value = "/api/trailer/getTrailerInfoByMainOrderNos")
    public ApiResult getTrailerInfoByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);
}
