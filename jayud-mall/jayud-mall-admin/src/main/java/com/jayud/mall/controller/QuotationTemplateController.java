package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryQuotationTemplateForm;
import com.jayud.mall.model.bo.QuotationTemplateForm;
import com.jayud.mall.model.bo.QuotationTemplateParaForm;
import com.jayud.mall.model.vo.QuotationTemplateVO;
import com.jayud.mall.service.IQuotationTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/quotationtemplate")
@Api(tags = "报价模板接口")
@ApiSort(value = 10002)
public class QuotationTemplateController {

    @Autowired
    IQuotationTemplateService quotationTemplateService;

    @ApiOperation(value = "分页查询报价模板")
    @PostMapping("/findQuotationTemplateByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<QuotationTemplateVO>> findQuotationTemplateByPage(@RequestBody QueryQuotationTemplateForm form) {
        IPage<QuotationTemplateVO> pageList = quotationTemplateService.findQuotationTemplateByPage(form);
        CommonPageResult<QuotationTemplateVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "禁用报价模板")
    @PostMapping(value = "/disabledQuotationTemplate")
    @ApiOperationSupport(order = 2)
    public CommonResult disabledQuotationTemplate(@Valid @RequestBody QuotationTemplateParaForm form) {
        Long id = form.getId();
        quotationTemplateService.disabledQuotationTemplate(id);
        return CommonResult.success("禁用报价模板成功！");
    }

    @ApiOperation(value = "启用报价模板")
    @PostMapping(value = "/enableQuotationTemplate")
    @ApiOperationSupport(order = 3)
    public CommonResult enableQuotationTemplate(@Valid @RequestBody QuotationTemplateParaForm form) {
        Long id = form.getId();
        quotationTemplateService.enableQuotationTemplate(id);
        return CommonResult.success("启用报价模板成功！");
    }

    @ApiOperation(value = "保存报价模板")
    @PostMapping(value = "saveQuotationTemplate")
    @ApiOperationSupport(order = 4)
    public CommonResult saveQuotationTemplate(@RequestBody QuotationTemplateForm form){
        quotationTemplateService.saveQuotationTemplate(form);
        return CommonResult.success("保存报价模板，成功！");
    }

    @ApiOperation(value = "查看报价模板")
    @PostMapping(value = "lookQuotationTemplate")
    @ApiOperationSupport(order = 5)
    public CommonResult<QuotationTemplateVO> lookQuotationTemplate(@Valid @RequestBody QuotationTemplateParaForm form){
        Long id = form.getId();
        return quotationTemplateService.lookQuotationTemplate(id);
    }


}
