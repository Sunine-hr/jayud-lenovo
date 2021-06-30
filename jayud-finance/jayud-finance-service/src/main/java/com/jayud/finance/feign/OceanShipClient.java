package com.jayud.finance.feign;

import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * oms模块消费sea模块的接口
 */
@FeignClient(value = "jayud-ocean-ship-web")
public interface OceanShipClient {
    /**
     * 根据主订单号集合获取海运订单详情信息
     *
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderInfoByMainOrderNos")
    ApiResult getSeaOrderInfoByMainOrderNos(@RequestParam("mainOrderNos")  List<String> mainOrderNoList);
}
