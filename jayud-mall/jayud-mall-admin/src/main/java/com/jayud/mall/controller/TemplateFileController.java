package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.TemplateFileForm;
import com.jayud.mall.model.po.TemplateFile;
import com.jayud.mall.service.ITemplateFileService;
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
@RequestMapping("/templatefile")
@Api(tags = "S038-后台-模板对应模块信息接口")
@ApiSort(value = 38)
public class TemplateFileController {

    @Autowired
    ITemplateFileService templateFileService;

    @ApiOperation(value = "查询模板对应模块信息List")
    @PostMapping("/findTemplateFile")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<TemplateFile>> findTemplateFile(@RequestBody TemplateFileForm form) {
        List<TemplateFile> list = templateFileService.findTemplateFile(form);
        return CommonResult.success(list);
    }

}
