package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.oms.model.bo.InputAirOrderForm;
import com.jayud.oms.model.vo.InitChangeStatusVO;
import com.jayud.oms.model.vo.InputAirOrderVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 */
@FeignClient(value = "jayud-freight-air-web")
public interface FreightAirClient {

    /**
     * 创建空运单
     */
    @RequestMapping(value = "/api/airfreight/createOrder")
    ApiResult createOrder(@RequestBody InputAirOrderForm addAirOrderForm);

    /**
     * 获取空运订单号
     */
    @RequestMapping(value = "/api/airfreight/getAirOrderNo")
    ApiResult<InitChangeStatusVO> getAirOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 关闭空运订单
     */
    @RequestMapping(value = "/api/airfreight/closeAirOrder")
    ApiResult closeAirOrder(@RequestBody List<SubOrderCloseOpt> form);

    /**
     * 查询空运订单详情
     */
    @PostMapping(value = "/getAirOrderDetails")
    public ApiResult<InputAirOrderVO> getAirOrderDetails(@RequestParam("mainOrderNo") String mainOrderNo);
}
