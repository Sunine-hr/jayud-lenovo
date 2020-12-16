package com.jayud.airfreight.feign;


import com.jayud.common.ApiResult;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * oms模块消费tms模块的接口
 */
@FeignClient(value = "jayud-tms-web")
public interface TmsClient {

    /**
     * 保中港扩展字段
     */
    @RequestMapping(value = "/api/saveOrUpdateTmsExtensionField")
    public ApiResult saveOrUpdateTmsExtensionField(@RequestBody String json);

    /**
     * 根据第三方订单获取中港订单信息
     */
    @RequestMapping(value = "/api/getTmsOrderByThirdPartyOrderNo")
    public ApiResult getTmsOrderByThirdPartyOrderNo(@RequestParam("thirdPartyOrderNo") String thirdPartyOrderNo);


    /**
     * 根据中港订单删除派车信息
     */
    @RequestMapping(value = "/api/deleteDispatchInfoByOrderNo")
    public ApiResult deleteDispatchInfoByOrderNo(@RequestParam("orderNo") String orderNo) ;
}
