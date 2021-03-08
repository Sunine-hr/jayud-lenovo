package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.oms.model.bo.InputOrderInlandTransportForm;
import com.jayud.oms.model.vo.InputOrderInlandTPVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 内陆模块
 */
@FeignClient(value = "jayud-Inland-transport-web", configuration = FeignRequestInterceptor.class)
public interface InlandTpClient {

    /**
     * 新增/编辑内陆运输单
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/createOrder")
    public ApiResult<String> createOrder(@RequestBody InputOrderInlandTransportForm form);

    @ApiOperation(value = "查询内陆订单详情")
    @PostMapping(value = "/api/getOrderDetails")
    public ApiResult<InputOrderInlandTPVO> getOrderDetails(@RequestParam("mainOrderNo") String mainOrderNo);

}
