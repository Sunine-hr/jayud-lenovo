package com.jayud.oms.controller;


import com.jayud.common.CommonResult;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.AddContractQuotationForm;
import com.jayud.oms.model.po.ContractQuotation;
import com.jayud.oms.service.IContractQuotationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 合同报价 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
@RestController
@Api(tags = "合同报价")
@RequestMapping("/contractQuotation")
public class ContractQuotationController {

    @Autowired
    private IContractQuotationService contractQuotationService;


    @ApiOperation("新增/编辑合同报价")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody AddContractQuotationForm form) {

        return null;
    }

    @ApiOperation("自动生成编号")
    @PostMapping("/autoGenerateNum")
    public CommonResult<String> autoGenerateNum() {
        StringBuilder orderNo = new StringBuilder("BJ");
        orderNo.append(DateUtils.LocalDateTime2Str(LocalDateTime.now(), "yyyyMMdd"))
                .append(StringUtils.zeroComplement(4, this.contractQuotationService.count() + 1));
        return CommonResult.success(orderNo.toString());
    }

}

