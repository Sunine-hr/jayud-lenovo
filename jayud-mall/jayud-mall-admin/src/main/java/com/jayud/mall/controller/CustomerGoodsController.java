package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomerGoodsAuditForm;
import com.jayud.mall.model.bo.CustomerGoodsForm;
import com.jayud.mall.model.bo.CustomerGoodsIdForm;
import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import com.jayud.mall.model.vo.CustomerGoodsVO;
import com.jayud.mall.service.ICustomerGoodsService;
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
@RequestMapping("/customergoods")
@Api(tags = "A018-admin-客户商品接口")
@ApiSort(value = 18)
public class CustomerGoodsController {

    @Autowired
    ICustomerGoodsService customerGoodsService;

    @ApiOperation(value = "分页查询客户商品")
    @PostMapping("/findCustomerGoodsByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<CustomerGoodsVO>> findCustomerGoodsByPage(@RequestBody QueryCustomerGoodsForm form) {
        IPage<CustomerGoodsVO> pageList = customerGoodsService.findCustomerGoodsByPage(form);
        CommonPageResult<CustomerGoodsVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "审核客户商品（单个/批量）")
    @PostMapping("/auditCustomerGoods")
    @ApiOperationSupport(order = 2)
    public CommonResult auditCustomerGoods(@RequestBody CustomerGoodsAuditForm form) {
        return customerGoodsService.auditCustomerGoods(form);
    }

    @ApiOperation(value = "保存商品信息")
    @PostMapping("/saveCustomerGoods")
    @ApiOperationSupport(order = 3)
    public CommonResult<CustomerGoodsVO> saveCustomerGoods(@Valid @RequestBody CustomerGoodsForm form){
        CustomerGoodsVO customerGoodsVO = customerGoodsService.saveCustomerGoods(form);
        return CommonResult.success(customerGoodsVO);
    }

    @ApiOperation(value = "根据商品id，查看商品，及其商品服务费用")
    @PostMapping("/findCustomerGoodsCostById")
    @ApiOperationSupport(order = 3)
    public CommonResult<CustomerGoodsVO> findCustomerGoodsCostById(@Valid @RequestBody CustomerGoodsIdForm form){
        Integer id = form.getId();
        CustomerGoodsVO customerGoodsVO = customerGoodsService.findCustomerGoodsCostById(id);
        return CommonResult.success(customerGoodsVO);
    }



}
