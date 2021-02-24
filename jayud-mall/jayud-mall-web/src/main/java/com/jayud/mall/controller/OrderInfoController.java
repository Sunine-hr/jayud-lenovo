package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OrderInfoForm;
import com.jayud.mall.model.bo.QueryOrderInfoForm;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderinfo")
@Api(tags = "C013-client-产品订单表接口")
@ApiSort(value = 13)
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;
    @Autowired
    BaseService baseService;

    //订单下单-暂存订单
    @ApiOperation(value = "订单下单-暂存订单")
    @PostMapping("/temporaryStorageOrderInfo")
    @ApiOperationSupport(order = 1)
    public CommonResult<OrderInfoVO> temporaryStorageOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.temporaryStorageOrderInfo(form);
    }

    //订单下单-提交订单
    @ApiOperation(value = "订单下单-提交订单")
    @PostMapping("/submitOrderInfo")
    @ApiOperationSupport(order = 2)
    public CommonResult<OrderInfoVO> submitOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.submitOrderInfo(form);
    }

    //订单列表-草稿-取消
    @ApiOperation(value = "订单列表-草稿-取消")
    @PostMapping("/draftCancelOrderInfo")
    @ApiOperationSupport(order = 3)
    public CommonResult<OrderInfoVO> draftCancelOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.draftCancelOrderInfo(form);
    }

    //订单列表-查看订单详情
    @ApiOperation(value = "订单列表-查看订单详情")
    @PostMapping("/lookOrderInfo")
    @ApiOperationSupport(order = 4)
    public CommonResult<OrderInfoVO> lookOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.lookOrderInfo(form);
    }

    //订单列表-账单确认

    //分页查询订单列表
    @ApiOperation(value = "web端分页查询订单列表")
    @PostMapping("/findWebOrderInfoByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<OrderInfoVO>> findWebOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(customerUser.getId());//当前登录客户
        IPage<OrderInfoVO> pageList = orderInfoService.findWebOrderInfoByPage(form);
        CommonPageResult<OrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }







}
