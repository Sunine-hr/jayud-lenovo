package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.oms.model.bo.InputOrderInlandTransportForm;
import com.jayud.oms.model.vo.InitChangeStatusVO;
import com.jayud.oms.model.vo.InputOrderInlandTPVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


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

    /**
     * 查询内陆订单详情
     */
    @PostMapping(value = "/api/getOrderDetails")
    public ApiResult<InputOrderInlandTPVO> getOrderDetails(@RequestParam("mainOrderNo") String mainOrderNo);

    /**
     * 根据主订单号集合查询内陆订单
     */
    @PostMapping(value = "/api/getInlandOrderByMainOrderNos")
    public ApiResult getInlandOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos);

    /**
     * 获取内陆订单号
     */
    @RequestMapping(value = "/api/getOrderNo")
    public ApiResult<InitChangeStatusVO> getOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/api/closeOrder")
    public ApiResult closeOrder(@RequestBody List<SubOrderCloseOpt> form);

    /**
     * 获取内陆订单list
     */
    @RequestMapping(value = "/api/getOrderInlandTransportList")
    public ApiResult getOrderInlandTransportList(@RequestParam("pickUpTimeStart") String pickUpTimeStart, @RequestParam("pickUpTimeEnd") String pickUpTimeEnd, @RequestParam(value = "orderNo", required = false) String orderNo);

}
