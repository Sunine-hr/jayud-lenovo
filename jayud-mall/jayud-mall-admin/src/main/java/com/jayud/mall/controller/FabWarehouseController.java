package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.model.vo.FabWarehouseVO;
import com.jayud.mall.service.IFabWarehouseService;
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
@RequestMapping("/fabwarehouse")
@Api(tags = "A028-admin-应收FBA仓库接口")
@ApiSort(value = 28)
public class FabWarehouseController {

    @Autowired
    IFabWarehouseService fabWarehouseService;

    @ApiOperation(value = "分页查询应收FBA仓库")
    @PostMapping("/findFabWarehouseByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<FabWarehouseVO>> findFabWarehouseByPage(@RequestBody QueryFabWarehouseForm form) {
        IPage<FabWarehouseVO> pageList = fabWarehouseService.findFabWarehouseByPage(form);
        CommonPageResult<FabWarehouseVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询应收FBA仓库list(目的仓库)")
    @PostMapping("/findfabWarehouse")
    @ApiOperationSupport(order = 2)
    public CommonResult<List<FabWarehouseVO>> findfabWarehouse(@RequestBody FabWarehouseArgsForm form) {
        List<FabWarehouseVO> list = fabWarehouseService.findfabWarehouse(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "编辑-保存应收FBA仓库")
    @PostMapping("/saveFabWarehouse")
    @ApiOperationSupport(order = 3)
    public CommonResult saveFabWarehouse(@Valid @RequestBody FabWarehouseForm form) {
        return fabWarehouseService.saveFabWarehouse(form);
    }

    @ApiOperation(value = "删除应收FBA仓库")
    @PostMapping("/deleteFabWarehouse")
    @ApiOperationSupport(order = 4)
    public CommonResult deleteFabWarehouse(@Valid @RequestBody FabWarehouseParaForm form) {
        return fabWarehouseService.deleteFabWarehouse(form.getId());
    }

    @ApiOperation(value = "审核应收FBA仓库")
    @PostMapping("/auditFabWarehouse")
    @ApiOperationSupport(order = 5)
    public CommonResult<CustomerVO> auditFabWarehouse(@Valid @RequestBody FabWarehouseForm form){
        return fabWarehouseService.auditFabWarehouse(form);
    }

}
