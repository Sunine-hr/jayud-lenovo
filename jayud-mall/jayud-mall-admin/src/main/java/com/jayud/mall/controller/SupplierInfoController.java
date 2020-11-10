package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import com.jayud.mall.model.vo.SupplierInfoVO;
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
    public CommonResult<List<SupplierInfoVO>> findSupplierInfo(@RequestBody QuerySupplierInfoForm form) {
        List<SupplierInfoVO> list = supplierInfoService.findSupplierInfo(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "分页查询供应商信息")
    @PostMapping("/findSupplierInfoByPage")
    public CommonResult<CommonPageResult<SupplierInfoVO>> findSupplierInfoByPage(@RequestBody QuerySupplierInfoForm form) {
        IPage<SupplierInfoVO> pageList = supplierInfoService.findSupplierInfoByPage(form);
        CommonPageResult<SupplierInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


}
