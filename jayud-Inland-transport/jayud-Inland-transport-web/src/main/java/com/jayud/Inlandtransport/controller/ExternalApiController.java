package com.jayud.Inlandtransport.controller;


import com.jayud.Inlandtransport.model.bo.AddOrderInlandTransportForm;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.InputOrderInlandTPVO;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportDetails;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import com.jayud.common.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "内陆外部调用")
public class ExternalApiController {

    @Autowired
    private IOrderInlandTransportService orderInlandTransportService;


    @RequestMapping(value = "/api/createOrder")
    public ApiResult<String> createOrder(@RequestBody AddOrderInlandTransportForm form) {
        String orderNo = orderInlandTransportService.createOrder(form);
        return ApiResult.ok(orderNo);
    }

    @ApiOperation(value = "查询内陆订单详情")
    @PostMapping(value = "/api/getOrderDetails")
    public ApiResult<InputOrderInlandTPVO> getOrderDetails(@RequestParam("mainOrderNo") String mainOrderNo) {
        List<OrderInlandTransport> list = this.orderInlandTransportService.getByCondition(new OrderInlandTransport().setMainOrderNo(mainOrderNo));
        OrderInlandTransportDetails orderDetails = this.orderInlandTransportService.getOrderDetails(list.get(0).getId());
        InputOrderInlandTPVO tmp = ConvertUtil.convert(orderDetails, InputOrderInlandTPVO.class);
        return ApiResult.ok(tmp);
    }
}
