package com.jayud.scm.controller;


import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommodityForm;
import com.jayud.scm.service.ICommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 商品申报要素明细表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/commodityEntry")
@Api(tags = "商品申报要素管理")
public class CommodityEntryController {

    @Autowired
    private ICommodityService commodityService;

    @ApiOperation(value = "商品列表")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@Valid @RequestBody QueryCommodityForm commodityForm) {
        commodityForm.setTime();

        return CommonResult.success();
    }
}

