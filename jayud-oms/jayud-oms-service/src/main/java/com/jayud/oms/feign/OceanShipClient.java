package com.jayud.oms.feign;

import com.jayud.common.ApiResult;
import com.jayud.oms.model.bo.InputSeaOrderForm;
import com.jayud.oms.model.vo.InputSeaOrderVO;
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
     * 创建海运单
     */
    @RequestMapping(value = "/api/oceanship/createOrder")
    ApiResult<String> createOrder(@RequestBody InputSeaOrderForm inputSeaOrderForm);

    /**
     * 根据主订单号获取海运单信息
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderDetails")
    ApiResult<InputSeaOrderVO> getSeaOrderDetails(@RequestParam("orderNo") String orderNo);

    /**
     * 根据主订单号集合获取海运订单信息
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderByMainOrderNos")
    ApiResult getSeaOrderByMainOrderNos(@RequestBody List<String> mainOrderNoList);
}
