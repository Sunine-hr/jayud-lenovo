package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.oms.model.vo.InputOrderCustomsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * oms模块消费customs模块的接口
 */
@FeignClient(value = "jayud-customs-web")
public interface CustomsClient {

    /**
     * 获取报关单数量
     */
    @RequestMapping(value = "/api/getCustomsOrderNum")
    ApiResult<Integer> getCustomsOrderNum(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 获取报关订单详情
     * @param mainOrderId
     * @return
     */
    @RequestMapping(value = "/api/getCustomsDetail")
    ApiResult<InputOrderCustomsVO> getCustomsDetail(@RequestParam(value = "mainOrderId") Long mainOrderId);


}
