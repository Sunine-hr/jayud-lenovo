package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.SaveCounterCaseForm;
import com.jayud.mall.service.ICounterCaseService;
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
@RequestMapping("/countercase")
@Api(tags = "A040-admin-货柜对应运单箱号信息接口")
@ApiSort(value = 40)
public class CounterCaseController {

    @Autowired
    ICounterCaseService counterCaseService;

    //保存运单箱号关联货柜信息
    //1个货柜可以关联多个箱号
    //1个箱号仅能关联一个货柜(限制)
    @ApiOperation(value = "保存货柜关联运单箱号信息")
    @PostMapping("/saveCounterCase")
    @ApiOperationSupport(order = 1)
    public CommonResult saveCounterCase(@Valid @RequestBody SaveCounterCaseForm form) {
        return counterCaseService.saveCounterCase(form);
    }



}
