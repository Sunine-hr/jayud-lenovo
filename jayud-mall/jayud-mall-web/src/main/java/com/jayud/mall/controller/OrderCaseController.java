package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CreateOrderCaseForm;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.service.IOrderCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ordercase")
@Api(tags = "C011-C端-订单对应箱号信息接口")
@ApiSort(value = 11)
public class OrderCaseController {

    @Autowired
    IOrderCaseService orderCaseService;

    //批量添加箱号
    @ApiModelProperty(value = "批量添加箱号")
    @PostMapping("/createOrderCaseList")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<OrderCaseVO>> createOrderCaseList(@RequestBody CreateOrderCaseForm form){
        List<OrderCaseVO> orderCaseVOList = orderCaseService.createOrderCaseList(form);
        return CommonResult.success(orderCaseVOList);
    }


}
