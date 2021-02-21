package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.TemplateCopeReceivableForm;
import com.jayud.mall.model.po.TemplateCopeReceivable;
import com.jayud.mall.service.ITemplateCopeReceivableService;
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
@RequestMapping("/templatecopereceivable")
@Api(tags = "A033-admin-报价模板对应应收费用明细接口")
@ApiSort(value = 33)
public class TemplateCopeReceivableController {

    @Autowired
    ITemplateCopeReceivableService templateCopeReceivableService;

    @ApiOperation(value = "查询报价模板对应应收费用明细List")
    @PostMapping("/findTemplateCopeReceivable")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<TemplateCopeReceivable>> findTemplateCopeReceivable(@RequestBody TemplateCopeReceivableForm form) {
        List<TemplateCopeReceivable> list = templateCopeReceivableService.findTemplateCopeReceivable(form);
        return CommonResult.success(list);
    }

}
