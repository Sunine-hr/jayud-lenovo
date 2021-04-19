package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CreateOrderCaseForm;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.service.IOrderCaseService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/ordercase")
@Api(tags = "C011-client-订单对应箱号信息接口")
@ApiSort(value = 11)
public class OrderCaseController {

    @Autowired
    IOrderCaseService orderCaseService;


    //批量添加箱号
    @ApiOperation(value = "批量添加箱号(根据总箱数添加)")
    @PostMapping("/createOrderCaseList")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<OrderCaseVO>> createOrderCaseList(@Valid @RequestBody CreateOrderCaseForm form){
        Integer cartons = form.getCartons();// 总箱数
        BigDecimal weight = form.getWeight();// 每箱重量(KG)
        BigDecimal length = form.getLength();// 长(cm)
        BigDecimal width = form.getWidth();// 宽(cm)
        BigDecimal height = form.getHeight();// 高(cm)
        BigDecimal zore = new BigDecimal("0");
        if(cartons <= 0){
            return CommonResult.error(-1, "总箱数不能小于或等于零");
        }
        if(weight.compareTo(zore) == -1){
            return CommonResult.error(-1, "重量不能小于或等于零");
        }
        if(length.compareTo(zore) == -1){
            return CommonResult.error(-1, "长不能小于或等于零");
        }
        if(width.compareTo(zore) == -1){
            return CommonResult.error(-1, "宽不能小于或等于零");
        }
        if(height.compareTo(zore) == -1){
            return CommonResult.error(-1, "高不能小于或等于零");
        }
        List<OrderCaseVO> orderCaseVOList = orderCaseService.createOrderCaseList(form);
        return CommonResult.success(orderCaseVOList);
    }


}
