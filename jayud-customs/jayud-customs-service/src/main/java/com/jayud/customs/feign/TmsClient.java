package com.jayud.customs.feign;


import com.jayud.common.ApiResult;
import com.jayud.customs.model.vo.InputOrderTransportVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * customs模块消费tms模块的接口
 */
@FeignClient(value = "jayud-tms-web")
public interface TmsClient {
    /**
     * 获取中港订单详情
     *
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/getOrderTransport")
    ApiResult<InputOrderTransportVO> getOrderTransport(@RequestParam(value = "mainOrderNo") String mainOrderNo);
}
