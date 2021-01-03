package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QuotedFileForm;
import com.jayud.mall.model.po.QuotedFile;
import com.jayud.mall.model.vo.QuotedFileReturnVO;
import com.jayud.mall.service.IQuotedFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quotedfile")
@Api(tags = "S037-后台-报价对应的文件表接口")
@ApiSort(value = 37)
public class QuotedFileController {

    @Autowired
    IQuotedFileService quotedFileService;

    @ApiOperation(value = "查询报价对应的文件表List")
    @PostMapping("/findQuotedFile")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<QuotedFile>> findQuotedFile(@RequestBody QuotedFileForm form) {
        List<QuotedFile> list = quotedFileService.findQuotedFile(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "报价模板使用模板文件")
    @PostMapping("/findQuotedFileBy")
    @ApiOperationSupport(order = 2)
    public CommonResult<List<QuotedFileReturnVO>> findQuotedFileBy(@RequestBody QuotedFileForm form){
        List<QuotedFileReturnVO> quotedFileReturnVOS = quotedFileService.findQuotedFileBy(form);
        return CommonResult.success(quotedFileReturnVOS);
    }



}
