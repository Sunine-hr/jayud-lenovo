package com.jayud.oms.feign;

import com.jayud.common.ApiResult;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.oms.model.bo.InputSeaOrderForm;
import com.jayud.oms.model.bo.InputTrailerOrderFrom;
import com.jayud.oms.model.vo.InitChangeStatusVO;
import com.jayud.oms.model.vo.InputSeaOrderVO;
import com.jayud.oms.model.vo.InputTrailerOrderVO;
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
     * 创建拖车单
     */
    @RequestMapping(value = "/api/trailer/createOrder")
    ApiResult<String> createOrder(@RequestBody InputTrailerOrderFrom inputTrailerOrderFrom);

    /**
     * 根据主订单号获取拖车单信息
     */
    @RequestMapping(value = "/api/trailer/getTrailerOrderDetails")
    ApiResult<List<InputTrailerOrderVO>> getTrailerOrderDetails(@RequestParam("orderNo") String orderNo);

    /**
     * 根据主订单号集合获取拖车订单信息
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/trailer/getTrailerOrderByMainOrderNos")
    ApiResult getTrailerOrderByMainOrderNos(@RequestBody List<String> mainOrderNoList);

    /**
     * 删除订单信息
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/trailer/deleteOrderByMainOrderNo")
    ApiResult deleteOrderByMainOrderNo(@RequestParam("mainOrderNo")String mainOrderNo);

    /**
     * 删除该主订单对应的商品和地址信息
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/trailer/getOrderNosByMainOrderNo")
    ApiResult<List<String>> getOrderNosByMainOrderNo(@RequestParam("mainOrderNo") String mainOrderNo);

    /**
     * 获取拖车订单号
     */
    @RequestMapping(value = "/api/trailer/getOrderNo")
    public ApiResult<List<InitChangeStatusVO>> getTrailerOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/api/closeOrder")
    public ApiResult closeOrder(@RequestBody List<SubOrderCloseOpt> form);
}
