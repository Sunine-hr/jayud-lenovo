package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.SupplierServeVO;
import com.jayud.mall.service.ISupplierServeService;
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
@RequestMapping("/supplierserve")
@Api(tags = "A015-admin-供应商服务接口")
@ApiSort(value = 15)
public class SupplierServeController {

    @Autowired
    ISupplierServeService supplierServeService;

    @ApiOperation(value = "分页查询供应商服务")
    @PostMapping("/findSupplierServeByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<SupplierServeVO>> findSupplierServeByPage(@RequestBody QuerySupplierServeForm form) {
        IPage<SupplierServeVO> pageList = supplierServeService.findSupplierServeByPage(form);
        CommonPageResult<SupplierServeVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存供应商服务")
    @PostMapping("/saveSupplierServe")
    @ApiOperationSupport(order = 2)
    public CommonResult saveSupplierServe(@RequestBody SupplierServeForm form){
        return supplierServeService.saveSupplierServe(form);
    }

    @ApiOperation(value = "查询供应商服务")
    @PostMapping("/findSupplierServeById")
    @ApiOperationSupport(order = 3)
    public CommonResult<SupplierServeVO> findSupplierServeById(@Valid @RequestBody SupplierServeIdForm form){
        Long id = form.getId();
        SupplierServeVO supplierServeVO = supplierServeService.findSupplierServeById(id);
        return CommonResult.success(supplierServeVO);
    }

    @ApiOperation(value = "禁用启用,供应商服务")
    @PostMapping("/disableEnabledSupplierServe")
    @ApiOperationSupport(order = 4)
    public CommonResult disableEnabledSupplierServe(@Valid @RequestBody SupplierServeStatusForm form){
        supplierServeService.disableEnabledSupplierServe(form);
        return CommonResult.success("操作成功");
    }

    @ApiOperation(value = "根据供应商id，查询供应商服务")
    @PostMapping("/findSupplierServeBySupplierInfoId")
    @ApiOperationSupport(order = 5)
    public CommonResult<List<SupplierServeVO>> findSupplierServeBySupplierInfoId(@Valid @RequestBody SupplierServeSupplierInfoIdForm form){
        Long supplierInfoId = form.getSupplierInfoId();
        List<SupplierServeVO> supplierServeVOS = supplierServeService.findSupplierServeBySupplierInfoId(supplierInfoId);
        return CommonResult.success(supplierServeVOS);
    }



}
