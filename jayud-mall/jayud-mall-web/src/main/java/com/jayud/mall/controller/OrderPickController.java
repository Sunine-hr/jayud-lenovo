package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OrderInfoParaForm;
import com.jayud.mall.model.bo.OrderPickIdForm;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.model.vo.OrderWarehouseNoVO;
import com.jayud.mall.service.IOrderPickService;
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

@RestController
@RequestMapping("/orderpick")
@Api(tags = "C012-client-订单对应提货信息表接口")
@ApiSort(value = 12)
public class OrderPickController {

    @Autowired
    IOrderPickService orderPickService;

    @ApiOperation(value = "批量创建-订单对应提货地址(进仓单号)")
    @ApiOperationSupport(order = 1)
    @PostMapping("/createOrderPickList")
    public CommonResult<List<OrderPickVO>> createOrderPickList(@RequestBody List<DeliveryAddressVO> form){
        List<OrderPickVO> orderPickVOList = orderPickService.createOrderPickList(form);
        return CommonResult.success(orderPickVOList);
    }

    //提货地址-下载进仓单号
    @ApiOperation(value = "提货地址-下载进仓单号")
    @ApiOperationSupport(order = 2)
    @PostMapping("/downloadWarehouseNoByPickId")
    public CommonResult<OrderWarehouseNoVO> downloadWarehouseNoByPickId(@Valid @RequestBody OrderPickIdForm form){
        Long id = form.getId();
        OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByPickId(id);
        return CommonResult.success(orderWarehouseNoVO);
    }

    //订单-下载进仓(不存在提货地址时)
    @ApiOperation(value = "订单-下载进仓(不存在提货地址时)")
    @ApiOperationSupport(order = 3)
    @PostMapping("/downloadWarehouseNoByOrderId")
    public CommonResult<OrderWarehouseNoVO> downloadWarehouseNoByOrderId(@Valid @RequestBody OrderInfoParaForm form){
        Long id = form.getId();
        OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByOrderId(id);
        return CommonResult.success(orderWarehouseNoVO);
    }

}
