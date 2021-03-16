package com.jayud.oceanship.controller;

import com.jayud.common.ApiResult;
import com.jayud.oceanship.bo.AddSeaOrderForm;
import com.jayud.oceanship.po.SeaOrder;
import com.jayud.oceanship.service.ISeaOrderService;
import com.jayud.oceanship.vo.SeaOrderVO;
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
@Api(tags = "海运被外部调用的接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    private ISeaOrderService seaOrderService;

    /**
     * 创建海运单
     * @param addSeaOrderForm
     * @return
     */
    @RequestMapping(value = "/api/oceanship/createOrder")
    public ApiResult<String> createOrder(@RequestBody AddSeaOrderForm addSeaOrderForm) {
        String orderNo = seaOrderService.createOrder(addSeaOrderForm);
        return ApiResult.ok(orderNo);
    }

    /**
     * 根据主订单号获取海运单信息
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderDetails")
    ApiResult<SeaOrderVO> getSeaOrderDetails(@RequestParam("orderNo")String orderNo){
        SeaOrder seaOrder = seaOrderService.getByMainOrderNO(orderNo);
        SeaOrderVO seaOrderVO = seaOrderService.getSeaOrderByOrderNO(seaOrder.getId());
        return ApiResult.ok(seaOrderVO);
    }

    /**
     * 根据主订单号集合获取海运订单信息
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderByMainOrderNos")
    ApiResult getSeaOrderByMainOrderNos(@RequestBody List<String> mainOrderNoList){
        List<SeaOrder> seaOrders = this.seaOrderService.getSeaOrderByOrderNOs(mainOrderNoList);
        return ApiResult.ok(seaOrders);
    }

}
