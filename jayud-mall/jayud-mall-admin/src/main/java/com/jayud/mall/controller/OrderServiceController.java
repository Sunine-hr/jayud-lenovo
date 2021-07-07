package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OrderServiceForm;
import com.jayud.mall.model.bo.OrderServiceIdForm;
import com.jayud.mall.model.bo.OrderServiceOrderIdForm;
import com.jayud.mall.model.vo.OrderServiceVO;
import com.jayud.mall.service.IOrderServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "A066-admin-订单服务接口")
@ApiSort(value = 66)
@RestController
@RequestMapping("/orderservice")
public class OrderServiceController {

    @Autowired
    IOrderServiceService orderServiceService;

    @ApiOperation(value = "根据订单id，查询订单服务列表list")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findOrderServiceByOrderId")
    public CommonResult<List<OrderServiceVO>> findOrderServiceByOrderId(@Valid @RequestBody OrderServiceOrderIdForm form){
        Long orderId = form.getOrderId();
        List<OrderServiceVO> orderServiceList = orderServiceService.findOrderServiceByOrderId(orderId);
        return CommonResult.success(orderServiceList);
    }

    @ApiOperation(value = "保存订单服务")
    @ApiOperationSupport(order = 2)
    @PostMapping("/saveOrderService")
    public CommonResult saveOrderService(@Valid @RequestBody OrderServiceForm form){
        orderServiceService.saveOrderService(form);
        return CommonResult.success("保存成功");
    }

    @ApiOperation(value = "根据服务id，查询订单服务")
    @ApiOperationSupport(order = 3)
    @PostMapping("/findOrderServiceById")
    public CommonResult<OrderServiceVO> findOrderServiceById(@Valid @RequestBody OrderServiceIdForm form){
        Long id = form.getId();
        OrderServiceVO orderServiceVO = orderServiceService.findOrderServiceById(id);
        return CommonResult.success(orderServiceVO);
    }




}