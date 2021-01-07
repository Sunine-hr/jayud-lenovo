package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanBillForm;
import com.jayud.mall.model.bo.OrderConfForm;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.vo.OrderConfVO;
import com.jayud.mall.service.INumberGeneratedService;
import com.jayud.mall.service.IOrderConfService;
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
@RequestMapping("/orderconf")
@Api(tags = "S043-后台-配载单接口")
@ApiSort(value = 43)
public class OrderConfController {

    @Autowired
    IOrderConfService orderConfService;
    @Autowired
    INumberGeneratedService numberGeneratedService;

    @ApiOperation(value = "分页查询配载单")
    @PostMapping("/findOrderConfByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<OrderConfVO>> findOrderConfByPage(@RequestBody QueryOrderConfForm form) {
        IPage<OrderConfVO> pageList = orderConfService.findOrderConfByPage(form);
        CommonPageResult<OrderConfVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存配载单")
    @PostMapping(value = "saveOrderConf")
    @ApiOperationSupport(order = 2)
    public CommonResult saveOrderConf(@RequestBody OrderConfForm form){
        orderConfService.saveOrderConf(form);
        return CommonResult.success("保存配载单，成功！");
    }

    @ApiOperation(value = "查看配载单详情")
    @PostMapping(value = "lookOrderConf")
    @ApiOperationSupport(order = 3)
    public CommonResult<OrderConfVO> lookOrderConf(@RequestBody OceanBillForm form){
        Long id = form.getId();
        return orderConfService.lookOrderConf(id);
    }

    //获取配载单号
    @ApiOperation(value = "获取配载单号")
    @PostMapping(value = "getOrderConfNo")
    @ApiOperationSupport(order = 4)
    public CommonResult<String> getOrderConfNo(){
        String orderNo = numberGeneratedService.getOrderNoByCode("order_conf_code");
        return CommonResult.success(orderNo);
    }

}
