package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import com.jayud.mall.model.bo.SupplierInfoForm;
import com.jayud.mall.model.bo.SupplierInfoParaForm;
import com.jayud.mall.model.vo.SupplierInfoVO;
import com.jayud.mall.model.vo.SupplierServeVO;
import com.jayud.mall.service.ISupplierInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/supplierinfo")
@Api(tags = "A014-admin-供应商信息接口")
@ApiSort(value = 14)
public class SupplierInfoController {

    @Autowired
    ISupplierInfoService supplierInfoService;

    @ApiOperation(value = "查询供应商信息List")
    @PostMapping("/findSupplierInfo")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<SupplierInfoVO>> findSupplierInfo(@RequestBody QuerySupplierInfoForm form) {
        List<SupplierInfoVO> list = supplierInfoService.findSupplierInfo(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "分页查询供应商信息")
    @PostMapping("/findSupplierInfoByPage")
    @ApiOperationSupport(order = 2)
    public CommonResult<CommonPageResult<SupplierInfoVO>> findSupplierInfoByPage(@RequestBody QuerySupplierInfoForm form) {
        IPage<SupplierInfoVO> pageList = supplierInfoService.findSupplierInfoByPage(form);
        CommonPageResult<SupplierInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存供应商信息")
    @PostMapping("/saveSupplierInfo")
    @ApiOperationSupport(order = 3)
    public CommonResult saveSupplierInfo(@RequestBody SupplierInfoForm form) {
        return supplierInfoService.saveSupplierInfo(form);
    }

    @ApiOperation(value = "查询供应商详细")
    @PostMapping("/findSupplierInfoById")
    @ApiOperationSupport(order = 4)
    public CommonResult<SupplierInfoVO> findSupplierInfoById(@Valid @RequestBody SupplierInfoParaForm form){
        Long id = form.getId();
        return supplierInfoService.findSupplierInfoById(id);
    }


    @ApiOperation(value = "启用供应商信息")
    @PostMapping("/enableSupplierInfo")
    @ApiOperationSupport(order = 5)
    public CommonResult<SupplierInfoVO> enableSupplierInfo(@Valid @RequestBody SupplierInfoParaForm form){
        Long id = form.getId();
        return supplierInfoService.enableSupplierInfo(id);
    }

    @ApiOperation(value = "禁用供应商信息")
    @PostMapping("/disableSupplierInfo")
    @ApiOperationSupport(order = 6)
    public CommonResult<SupplierInfoVO> disableSupplierInfo(@Valid @RequestBody SupplierInfoParaForm form){
        Long id = form.getId();
        return supplierInfoService.disableSupplierInfo(id);
    }


    @ApiOperation(value = "查询供应商的服务和费用详细")
    @PostMapping("/findSupplierSerCostInfoById")
    @ApiOperationSupport(order = 7)
    public CommonResult<List<SupplierServeVO>> findSupplierSerCostInfoById(@Valid @RequestBody SupplierInfoParaForm form){
        Long supplierInfoId = form.getId();
        List<SupplierServeVO> supplierServeVOS = supplierInfoService.findSupplierSerCostInfoById(supplierInfoId);
        return CommonResult.success(supplierServeVOS);
    }



}
