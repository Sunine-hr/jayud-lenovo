package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OrderCaseWmsCartonNoForm;
import com.jayud.mall.model.bo.QueryOrderCaseWmsForm;
import com.jayud.mall.model.vo.OrderCaseWmsVO;
import com.jayud.mall.service.IOrderCaseWmsService;
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

@Api(tags = "A063-admin-订单装箱接口")
@ApiSort(value = 63)
@RestController
@RequestMapping("/ordercasewms")
public class OrderCaseWmsController {

    @Autowired
    IOrderCaseWmsService orderCaseWmsService;

    @ApiOperation(value = "分页查询-订单装箱信息")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findOrderCaseWmsPage")
    public CommonResult<CommonPageResult<OrderCaseWmsVO>> findOrderCaseWmsPage(@Valid @RequestBody QueryOrderCaseWmsForm form) {
        IPage<OrderCaseWmsVO> pageList = orderCaseWmsService.findOrderCaseWmsPage(form);
        CommonPageResult<OrderCaseWmsVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "根据箱号，查询订单装箱信息")
    @ApiOperationSupport(order = 2)
    @PostMapping("/findOrderCaseWmsByCartonNo")
    public CommonResult<OrderCaseWmsVO> findOrderCaseWmsByCartonNo(@Valid @RequestBody OrderCaseWmsCartonNoForm form){
        String cartonNo = form.getCartonNo();
        OrderCaseWmsVO orderCaseWmsVO = orderCaseWmsService.findOrderCaseWmsByCartonNo(cartonNo);
        return CommonResult.success(orderCaseWmsVO);
    }


}
