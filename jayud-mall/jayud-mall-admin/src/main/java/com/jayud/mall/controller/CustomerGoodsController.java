package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomerGoodsForm;
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

@RestController
@RequestMapping("/customergoods")
@Api(tags = "S021-后台-客户商品接口")
@ApiSort(value = 21)
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

    @ApiOperation(value = "审核客户商品")
    @PostMapping("/auditCustomerGoods")
    @ApiOperationSupport(order = 2)
    public CommonResult auditCustomerGoods(@RequestBody CustomerGoodsForm form) {
        return customerGoodsService.auditCustomerGoods(form);
    }




}
