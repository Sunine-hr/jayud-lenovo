package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.vo.SupplierServiceTypeVO;
import com.jayud.mall.service.ISupplierServiceTypeService;
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
@RequestMapping("/supplierservicetype")
@Api(tags = "S013-后台-供应商服务类型接口")
@ApiSort(value = 13)
public class SupplierServiceTypeController {

    @Autowired
    ISupplierServiceTypeService supplierServiceTypeService;

    @ApiOperation(value = "供应商服务类型list")
    @PostMapping("/findSupplierServiceType")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<SupplierServiceTypeVO>> findSupplierServiceType() {
        List<SupplierServiceTypeVO> list = supplierServiceTypeService.findSupplierServiceType();
        return CommonResult.success(list);
    }

}
