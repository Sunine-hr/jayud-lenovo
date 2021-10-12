package com.jayud.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.QueryCustomsFinanceCoRelationForm;
import com.jayud.finance.po.CustomsFinanceCoRelation;
import com.jayud.finance.service.ICustomsFinanceCoRelationService;
import com.jayud.finance.vo.FinanceAccountVO;
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

    @ApiOperation(value = "查询公司关联page")
    @PostMapping("/findCompanyRelationPage")
    public CommonResult<CommonPageResult<CustomsFinanceCoRelation>> findCompanyRelationPage(@RequestBody QueryCustomsFinanceCoRelationForm form){
        IPage<CustomsFinanceCoRelation> pageList = customsFinanceCoRelationService.findCompanyRelationPage(form);
        CommonPageResult<CustomsFinanceCoRelation> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询公司关联list")
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
