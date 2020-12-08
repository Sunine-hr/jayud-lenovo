package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.vo.QuotationTypeReturnVO;
import com.jayud.mall.service.IQuotationTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quotationtype")
@Api(tags = "报价类型表接口")
public class QuotationTypeController {

    @Autowired
    IQuotationTypeService quotationTypeService;

    @ApiOperation(value = "报价类型下拉选择")
    @PostMapping("/findQuotationTypeBy")
    public CommonResult<QuotationTypeReturnVO> findQuotationTypeBy(){
        QuotationTypeReturnVO quotationTypeReturnVO = quotationTypeService.findQuotationTypeBy();
        return CommonResult.success(quotationTypeReturnVO);
    }


}
