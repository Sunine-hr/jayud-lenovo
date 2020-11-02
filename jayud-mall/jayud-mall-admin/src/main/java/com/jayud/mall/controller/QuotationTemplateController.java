package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryQuotationTemplateFrom;
import com.jayud.mall.model.vo.QuotationTemplateVO;
import com.jayud.mall.service.IQuotationTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quotationtemplate")
@Api(tags = "报价模板接口")
public class QuotationTemplateController {

    @Autowired
    IQuotationTemplateService quotationTemplateService;

    @ApiOperation(value = "分页查询报价模板模板")
    @PostMapping("/findQuotationTemplateByPage")
    public CommonResult<CommonPageResult<QuotationTemplateVO>> findQuotationTemplateByPage(@RequestBody QueryQuotationTemplateFrom form) {
        IPage<QuotationTemplateVO> pageList = quotationTemplateService.findQuotationTemplateByPage(form);
        CommonPageResult<QuotationTemplateVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

}
