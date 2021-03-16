package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.AuditTradingRecordCZForm;
import com.jayud.mall.model.bo.QueryTradingRecordCZForm;
import com.jayud.mall.model.vo.TradingRecordVO;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ITradingRecordService;
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
@RequestMapping("/tradingrecord")
@Api(tags = "A049-client-交易记录接口")
@ApiSort(value = 49)
public class TradingRecordController {

    @Autowired
    ITradingRecordService tradingRecordService;
    @Autowired
    BaseService baseService;


    //财务管理-充值审核-分页查询
    @ApiOperation(value = "财务管理-充值审核-分页查询")
    @PostMapping("/findTradingRecordCZByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<TradingRecordVO>> findTradingRecordCZByPage(@RequestBody QueryTradingRecordCZForm form) {
        IPage<TradingRecordVO> pageList = tradingRecordService.findTradingRecordCZByPage(form);
        CommonPageResult<TradingRecordVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //财务管理-充值审核-审核
    @ApiOperation(value = "财务管理-充值审核-审核")
    @PostMapping("/auditTradingRecordCZ")
    @ApiOperationSupport(order = 2)
    public CommonResult<TradingRecordVO> auditTradingRecordCZ(@Valid @RequestBody AuditTradingRecordCZForm form){
        return tradingRecordService.auditTradingRecordCZ(form);
    }




}
