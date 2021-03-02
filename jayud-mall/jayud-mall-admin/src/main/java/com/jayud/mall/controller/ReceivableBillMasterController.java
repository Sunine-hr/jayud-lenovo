package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryReceivableBillMasterForm;
import com.jayud.mall.model.bo.ReceivableBillForm;
import com.jayud.mall.model.bo.ReceivableBillMasterForm;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;
import com.jayud.mall.service.IReceivableBillMasterService;
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
@RequestMapping("/receivablebillmaster")
@Api(tags = "A046-admin-应收账单接口")
@ApiSort(value = 46)
public class ReceivableBillMasterController {

    @Autowired
    IReceivableBillMasterService receivableBillMasterService;


    //生成应收账单
    @ApiOperation(value = "生成应收账单")
    @PostMapping("/createReceivableBill")
    @ApiOperationSupport(order = 1)
    public CommonResult<ReceivableBillMasterVO> createReceivableBill(@Valid @RequestBody ReceivableBillForm form){
        return receivableBillMasterService.createReceivableBill(form);
    }

    //生成应收账单-确认
    //affirm
    @ApiOperation(value = "生成应收账单-确认")
    @PostMapping("/affirmReceivableBill")
    @ApiOperationSupport(order = 2)
    public CommonResult<ReceivableBillMasterVO> affirmReceivableBill(@Valid @RequestBody ReceivableBillMasterForm form){
        return receivableBillMasterService.affirmReceivableBill(form);
    }

    //生成应收账单-取消（前端取消）

    //应收账单分页查询
    @ApiOperation(value = "应收账单分页查询")
    @PostMapping("/findReceivableBillMasterByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<ReceivableBillMasterVO>> findReceivableBillMasterByPage(
            @RequestBody QueryReceivableBillMasterForm form) {
        IPage<ReceivableBillMasterVO> pageList = receivableBillMasterService.findReceivableBillMasterByPage(form);
        CommonPageResult<ReceivableBillMasterVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }



}