package com.jayud.trailer.controller;

import com.jayud.common.ApiResult;
import com.jayud.trailer.bo.AddTrailerOrderFrom;
import com.jayud.trailer.po.TrailerOrder;
import com.jayud.trailer.service.ITrailerOrderService;
import com.jayud.trailer.vo.TrailerOrderVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 创建拖车单
     * @param addTrailerOrderFrom
     * @return
     */
    @RequestMapping(value = "/api/trailer/createOrder")
    public ApiResult createOrder(@RequestBody AddTrailerOrderFrom addTrailerOrderFrom) {
        trailerOrderService.createOrder(addTrailerOrderFrom);
        return ApiResult.ok();
    }

    /**
     * 根据主订单号获取拖车订单信息
     */
    @RequestMapping(value = "/api/trailer/getTrailerOrderDetails")
    ApiResult<TrailerOrderVO> getSeaOrderDetails(@RequestParam("orderNo")String orderNo){
        TrailerOrder trailerOrder = trailerOrderService.getByMainOrderNO(orderNo);
        TrailerOrderVO trailerOrderVO = trailerOrderService.getTrailerOrderByOrderNO(trailerOrder.getId());
        return ApiResult.ok(trailerOrderVO);
    }

    /**
     * 根据主订单号集合获取拖车订单信息
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/trailer/getTrailerOrderByMainOrderNos")
    ApiResult getSeaOrderByMainOrderNos(@RequestBody List<String> mainOrderNoList){
        List<TrailerOrder> seaOrders = this.trailerOrderService.getTrailerOrderByOrderNOs(mainOrderNoList);
        return ApiResult.ok(seaOrders);
    }

}
