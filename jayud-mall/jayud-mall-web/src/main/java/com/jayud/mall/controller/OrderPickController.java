package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.service.IOrderPickService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderpick")
@Api(tags = "C012-client-订单对应提货信息表接口")
@ApiSort(value = 12)
public class OrderPickController {

    @Autowired
    IOrderPickService orderPickService;

    @ApiOperation(value = "批量创建-订单对应提货地址(进仓单号)")
    @PostMapping("/createOrderPickList")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<OrderPickVO>> createOrderPickList(@RequestBody List<DeliveryAddressVO> form){
        List<OrderPickVO> orderPickVOList = orderPickService.createOrderPickList(form);
        return CommonResult.success(orderPickVOList);
    }


}
