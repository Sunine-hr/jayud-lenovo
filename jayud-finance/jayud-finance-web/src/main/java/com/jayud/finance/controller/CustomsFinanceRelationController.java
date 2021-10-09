package com.jayud.finance.controller;

import com.jayud.common.CommonResult;
import com.jayud.finance.po.CustomsFinanceCoRelation;
import com.jayud.finance.service.ICustomsFinanceCoRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customsFinanceRelation/")
@Api(tags = "云报关-财务金蝶对应")
public class CustomsFinanceRelationController {

    @Autowired
    private ICustomsFinanceCoRelationService customsFinanceCoRelationService;

    @ApiOperation(value = "查询公司关联")
    @PostMapping("/companyRelationList")
    public CommonResult<List<CustomsFinanceCoRelation>> companyRelationList(){
        List<CustomsFinanceCoRelation> list = customsFinanceCoRelationService.list();
        return CommonResult.success(list);
    }

    @ApiOperation(value = "保存公司关联")
    @PostMapping("/saveCustomsFinanceCoRelation")
    public CommonResult saveCustomsFinanceCoRelation(@RequestBody CustomsFinanceCoRelation customsFinanceCoRelation){
        customsFinanceCoRelationService.saveCustomsFinanceCoRelation(customsFinanceCoRelation);
        return CommonResult.success();
    }

    @ApiOperation(value = "清理公司关联redis")
    @PostMapping("/clearCompanyRelationMap")
    public CommonResult clearCompanyRelationMap(){
        customsFinanceCoRelationService.clearCompanyRelationMap();
        return CommonResult.success();
    }

    @ApiOperation(value = "刷新公司关联redis")
    @PostMapping("/refreshCompanyRelationMap")
    public CommonResult refreshCompanyRelationMap(){
        Map<String, CustomsFinanceCoRelation> stringCustomsFinanceCoRelationMap = customsFinanceCoRelationService.refreshCompanyRelationMap();
        return CommonResult.success(stringCustomsFinanceCoRelationMap);
    }

    @ApiOperation(value = "获取公司关联redis")
    @PostMapping("/getCompanyRelationMap")
    public CommonResult getCompanyRelationMap(){
        Map<String, CustomsFinanceCoRelation> companyRelationMap = customsFinanceCoRelationService.getCompanyRelationMap();
        return CommonResult.success(companyRelationMap);
    }





}
