package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.TemplateCopeWithForm;
import com.jayud.mall.model.po.TemplateCopeWith;
import com.jayud.mall.service.ITemplateCopeWithService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/templatecopewith")
@Api(tags = "报价对应应付费用明细接口")
public class TemplateCopeWithController {

    @Autowired
    ITemplateCopeWithService templateCopeWithService;

    @ApiOperation(value = "查询报价对应应付费用明细List")
    @PostMapping("/findTemplateCopeWith")
    public CommonResult<List<TemplateCopeWith>> findTemplateCopeWith(@RequestBody TemplateCopeWithForm form) {
        List<TemplateCopeWith> list = templateCopeWithService.findTemplateCopeWith(form);
        return CommonResult.success(list);
    }



}
