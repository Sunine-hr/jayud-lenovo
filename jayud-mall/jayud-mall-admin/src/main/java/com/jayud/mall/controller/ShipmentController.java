package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryShipmentForm;
import com.jayud.mall.model.bo.QueryShipmentParaForm;
import com.jayud.mall.model.vo.ShipmentVO;
import com.jayud.mall.service.IShipmentService;
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

@RestController
@RequestMapping("/shipment")
@Api(tags = "A053-admin-南京新智慧订单装货接口")
@ApiSort(value = 53)
public class ShipmentController {

    @Autowired
    IShipmentService shipmentService;

    @ApiOperation(value = "分页查询订单装货信息")
    @PostMapping("/findShipmentByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<ShipmentVO>> findShipmentByPage(@RequestBody QueryShipmentForm form) {
        IPage<ShipmentVO> pageList = shipmentService.findShipmentByPage(form);
        CommonPageResult<ShipmentVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询订单装货信息")
    @PostMapping("/findShipmentById")
    @ApiOperationSupport(order = 2)
    public CommonResult<ShipmentVO> findShipmentById(@Valid @RequestBody QueryShipmentParaForm form){
        String shipment_id = form.getShipment_id();
        return shipmentService.findShipmentById(shipment_id);
    }

    //根据新智慧运单生成南京订单(订单、订单商品、订单箱号)
    @ApiOperation(value = "根据新智慧运单生成南京订单(订单、订单商品、订单箱号)")
    @PostMapping("/createOrderByShipment")
    @ApiOperationSupport(order = 3)
    public CommonResult<ShipmentVO> createOrderByShipment(@Valid @RequestBody QueryShipmentParaForm form){
        String shipment_id = form.getShipment_id();
        return shipmentService.createOrderByShipment(shipment_id);
    }



}
