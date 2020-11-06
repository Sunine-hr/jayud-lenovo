package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.SupplierInfoForm;
import com.jayud.mall.model.po.SupplierInfo;
import com.jayud.mall.service.ISupplierInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/supplierinfo")
@Api(tags = "供应商信息接口")
public class SupplierInfoController {

    @Autowired
    ISupplierInfoService supplierInfoService;

    @ApiOperation(value = "查询供应商信息List")
    @PostMapping("/findSupplierInfo")
    public CommonResult<List<SupplierInfo>> findSupplierInfo(@RequestBody SupplierInfoForm form) {
        List<SupplierInfo> list = supplierInfoService.findSupplierInfo(form);
        return CommonResult.success(list);
    }

}
