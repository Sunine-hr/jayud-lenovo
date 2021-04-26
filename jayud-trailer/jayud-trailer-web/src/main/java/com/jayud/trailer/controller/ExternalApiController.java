package com.jayud.trailer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.trailer.bo.AddTrailerOrderFrom;
import com.jayud.trailer.feign.OmsClient;
import com.jayud.trailer.po.TrailerOrder;
import com.jayud.trailer.service.ITrailerOrderService;
import com.jayud.trailer.vo.TrailerOrderVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 被外部模块调用的处理接口
 *
 * @author
 * @description
 * @Date:
 */
@RestController
@Api(tags = "拖车被外部调用的接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    private ITrailerOrderService trailerOrderService;

    @Autowired
    private OmsClient omsClient;

    /**
     * 创建拖车单
     * @param addTrailerOrderFrom
     * @return
     */
    @RequestMapping(value = "/api/trailer/createOrder")
    public ApiResult<String> createOrder(@RequestBody AddTrailerOrderFrom addTrailerOrderFrom) {
        String order = trailerOrderService.createOrder(addTrailerOrderFrom);
        return ApiResult.ok(order);
    }

    /**
     * 根据主订单号获取拖车订单信息
     */
    @RequestMapping(value = "/api/trailer/getTrailerOrderDetails")
    ApiResult<List<TrailerOrderVO>> getTrailerOrderDetails(@RequestParam("orderNo")String orderNo){
        List<TrailerOrder> trailerOrders = trailerOrderService.getByMainOrderNO(orderNo);
        List<TrailerOrderVO> list = new ArrayList<>();
        for (TrailerOrder trailerOrder : trailerOrders) {
            TrailerOrderVO trailerOrderVO = trailerOrderService.getTrailerOrderByOrderNO(trailerOrder.getId());
            list.add(trailerOrderVO);
        }
        return ApiResult.ok(list);
    }

    /**
     * 根据主订单号集合获取拖车订单信息
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/trailer/getTrailerOrderByMainOrderNos")
    ApiResult getTrailerOrderByMainOrderNos(@RequestBody List<String> mainOrderNoList){
        List<List<TrailerOrder>> trailerOrderList = new ArrayList<>();
        for (String s : mainOrderNoList) {
            List<TrailerOrder> trailerOrders = this.trailerOrderService.getByMainOrderNO(s);
            trailerOrderList.add(trailerOrders);
        }
        return ApiResult.ok(trailerOrderList);
    }

    @RequestMapping(value = "/api/trailer/deleteOrderByMainOrderNo")
    ApiResult deleteOrderByMainOrderNo(@RequestParam("mainOrderNo") String mainOrderNo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no",mainOrderNo);
        boolean remove = trailerOrderService.remove(queryWrapper);
        if(!remove){
            return ApiResult.error();
        }
        return ApiResult.ok();
    }

    /**
     * 删除该主订单对应的商品和地址信息
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/trailer/getOrderNosByMainOrderNo")
    ApiResult<List<String>> getOrderNosByMainOrderNo(@RequestParam("mainOrderNo") String mainOrderNo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no",mainOrderNo);
        List<TrailerOrder> byMainOrderNO = trailerOrderService.getByMainOrderNO(mainOrderNo);
        List<String> orderNos = new ArrayList<>();
        for (TrailerOrder trailerOrder : byMainOrderNO) {
            orderNos.add(trailerOrder.getOrderNo());
        }
        return ApiResult.ok(orderNos);
    }

}
