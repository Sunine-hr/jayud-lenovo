package com.jayud.customs.feign;

import com.jayud.common.ApiResult;
import com.jayud.customs.model.vo.InputSeaOrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * customs模块消费sea模块的接口
 */
@FeignClient(value = "jayud-ocean-ship-web")
public interface OceanShipClient {
    /**
     * 根据主订单号获取海运单信息
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderDetails")
    ApiResult<InputSeaOrderVO> getSeaOrderDetails(@RequestParam("orderNo") String orderNo);
}
