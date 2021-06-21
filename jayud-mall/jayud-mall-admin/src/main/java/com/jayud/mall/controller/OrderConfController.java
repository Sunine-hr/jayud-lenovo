package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.model.vo.OrderConfVO;
import com.jayud.mall.service.IOrderConfService;
import com.jayud.mall.utils.NumberGeneratedUtils;
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
@RequestMapping("/orderconf")
@Api(tags = "A042-admin-配载单接口")
@ApiSort(value = 42)
public class OrderConfController {

    @Autowired
    IOrderConfService orderConfService;

    @ApiOperation(value = "分页查询配载单")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findOrderConfByPage")
    public CommonResult<CommonPageResult<OrderConfVO>> findOrderConfByPage(@RequestBody QueryOrderConfForm form) {
        IPage<OrderConfVO> pageList = orderConfService.findOrderConfByPage(form);
        CommonPageResult<OrderConfVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存配载单")
    @ApiOperationSupport(order = 2)
    @PostMapping(value = "saveOrderConf")
    public CommonResult saveOrderConf(@RequestBody OrderConfForm form){
        orderConfService.saveOrderConf(form);
        return CommonResult.success("保存配载单，成功！");
    }

    @ApiOperation(value = "查看配载单详情")
    @ApiOperationSupport(order = 3)
    @PostMapping(value = "lookOrderConf")
    public CommonResult<OrderConfVO> lookOrderConf(@RequestBody OrderConfParaForm form){
        Long id = form.getId();
        return orderConfService.lookOrderConf(id);
    }

    //获取配载单号
    @ApiOperation(value = "获取配载单号(自动递增)")
    @ApiOperationSupport(order = 4)
    @PostMapping(value = "getOrderConfNo")
    public CommonResult<String> getOrderConfNo(){
        //String orderNo = numberGeneratedService.getOrderNoByCode("order_conf_code");
        String orderNo = NumberGeneratedUtils.getOrderNoByCode2("order_conf_code");
        return CommonResult.success(orderNo);
    }

    @ApiOperation(value = "保存配载单-关联报价")
    @ApiOperationSupport(order = 5)
    @PostMapping(value = "saveOrderConfByOfferInfo")
    public CommonResult<OrderConfVO> saveOrderConfByOfferInfo(@RequestBody OrderConfForm form){
        OrderConfVO orderConfVO = orderConfService.saveOrderConfByOfferInfo(form);
        return CommonResult.success(orderConfVO);
    }

    @ApiOperation(value = "保存配载单-关联提单")
    @ApiOperationSupport(order = 6)
    @PostMapping(value = "saveOrderConfByOceanBill")
    public CommonResult<OrderConfVO> saveOrderConfByOceanBill(@RequestBody OrderConfForm form){
        OrderConfVO orderConfVO = orderConfService.saveOrderConfByOceanBill(form);
        return CommonResult.success(orderConfVO);
    }

    @ApiOperation(value = "新增配载单（新增后在编辑，配载报价和提单）")
    @ApiOperationSupport(order = 7)
    @PostMapping(value = "addOrderConf")
    public CommonResult<OrderConfVO> addOrderConf(@RequestBody OrderConfForm form){
        OrderConfVO orderConfVO = orderConfService.addOrderConf(form);
        return CommonResult.success(orderConfVO);
    }

    @ApiOperation(value = "新增编辑查询配载：提单、报价、运单(订单)")
    @ApiOperationSupport(order = 8)
    @PostMapping(value = "findOrderConfById")
    public CommonResult<OrderConfVO> findOrderConfById(@Valid @RequestBody OrderConfIdForm form){
        Long id = form.getId();
        OrderConfVO orderConfVO = orderConfService.findOrderConfById(id);
        return CommonResult.success(orderConfVO);
    }

    @ApiOperation(value = "根据配载id，查询配载下所有的提单列表")
    @ApiOperationSupport(order = 9)
    @PostMapping(value = "findOceanBillByConfId")
    public CommonResult<List<OceanBillVO>> findOceanBillByConfId(@Valid @RequestBody OrderConfIdForm form){
        Long id = form.getId();
        List<OceanBillVO> oceanBillVOList = orderConfService.findOceanBillByConfId(id);
        return CommonResult.success(oceanBillVOList);
    }

    //配载单-启用按钮
    @ApiOperation(value = "配载单-启用按钮")
    @ApiOperationSupport(order = 10)
    @PostMapping(value = "enableStatus")
    public CommonResult enableStatus(@Valid @RequestBody OrderConfIdForm form){
        orderConfService.enableStatus(form);
        return CommonResult.success("操作成功");
    }

    //配载单-取消按钮
    @ApiOperation(value = "配载单-取消按钮")
    @ApiOperationSupport(order = 11)
    @PostMapping(value = "cancelStatus")
    public CommonResult cancelStatus(@Valid @RequestBody OrderConfIdForm form){
        orderConfService.cancelStatus(form);
        return CommonResult.success("操作成功");
    }

    //配载单-开始配载按钮
    @ApiOperation(value = "配载单-开始配载按钮")
    @ApiOperationSupport(order = 12)
    @PostMapping(value = "startAutostowStatus")
    public CommonResult startAutostowStatus(@Valid @RequestBody OrderConfIdForm form){
        orderConfService.startAutostowStatus(form);
        return CommonResult.success("操作成功");
    }

    //配载单-转运中按钮
    @ApiOperation(value = "配载单-转运中按钮")
    @ApiOperationSupport(order = 13)
    @PostMapping(value = "transitStatus")
    public CommonResult transitStatus(@Valid @RequestBody OrderConfIdForm form){
        orderConfService.transitStatus(form);
        return CommonResult.success("操作成功");
    }

    //配载单-完成按钮
    @ApiOperation(value = "配载单-完成按钮")
    @ApiOperationSupport(order = 14)
    @PostMapping(value = "finishStatus")
    public CommonResult finishStatus(@Valid @RequestBody OrderConfIdForm form){
        orderConfService.finishStatus(form);
        return CommonResult.success("操作成功");
    }

    //配载单-取消按钮前验证
    @ApiOperation(value = "配载单-取消按钮前验证")
    @ApiOperationSupport(order = 15)
    @PostMapping(value = "cancelStatusVerify")
    public CommonResult cancelStatusVerify(@Valid @RequestBody OrderConfVerifyForm form){
        orderConfService.cancelStatusVerify(form);
        return CommonResult.success("验证成功");
    }




}
