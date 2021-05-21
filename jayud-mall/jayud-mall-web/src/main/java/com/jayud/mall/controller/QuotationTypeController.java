package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.vo.QuotationTypeReturnVO;
import com.jayud.mall.service.IQuotationTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quotationtype")
@Api(tags = "C020-client-报价类型表接口")
@ApiSort(value = 20)
public class QuotationTypeController {

    @Autowired
    IQuotationTypeService quotationTypeService;

    @ApiOperation(value = "报价类型下拉选择，给前端用")
    @PostMapping("/findQuotationTypeBy")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<QuotationTypeReturnVO>> findQuotationTypeBy(){
        List<QuotationTypeReturnVO> quotationTypeReturnVOList = quotationTypeService.findQuotationTypeBy();
        return CommonResult.success(quotationTypeReturnVOList);
    }

}
