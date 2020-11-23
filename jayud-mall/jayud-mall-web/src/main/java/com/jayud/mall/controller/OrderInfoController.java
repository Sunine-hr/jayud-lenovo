package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OrderInfoForm;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderinfo")
@Api(tags = "C端-产品订单表接口")
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;

    //订单下单-暂存订单
    @ApiModelProperty(value = "订单下单-暂存订单")
    @PostMapping("/temporaryStorageOrderInfo")
    public CommonResult<OrderInfoVO> temporaryStorageOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.temporaryStorageOrderInfo(form);
    }

    //订单下单-提交订单
    @ApiModelProperty(value = "订单下单-提交订单")
    @PostMapping("/submitOrderInfo")
    public CommonResult<OrderInfoVO> submitOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.submitOrderInfo(form);
    }

    //订单列表-草稿-提交
    @ApiModelProperty(value = "订单列表-草稿-提交")
    @PostMapping("/draftSubmitOrderInfo")
    public CommonResult<OrderInfoVO> draftSubmitOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.draftSubmitOrderInfo(form);
    }

    //订单列表-草稿-取消
    @ApiModelProperty(value = "订单列表-草稿-取消")
    @PostMapping("/draftCancelOrderInfo")
    public CommonResult<OrderInfoVO> draftCancelOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.draftCancelOrderInfo(form);
    }

    //订单列表-查看订单详情
    @ApiModelProperty(value = "订单列表-查看订单详情")
    @PostMapping("/lookOrderInfo")
    public CommonResult<OrderInfoVO> lookOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.lookOrderInfo(form);
    }

    //订单列表-账单确认







}
