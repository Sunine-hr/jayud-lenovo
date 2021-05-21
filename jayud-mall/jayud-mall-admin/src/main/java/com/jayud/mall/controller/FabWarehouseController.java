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
@Api(tags = "A028-admin-应收fab仓库接口")
@ApiSort(value = 28)
public class FabWarehouseController {

    @Autowired
    IFabWarehouseService fabWarehouseService;

    @ApiOperation(value = "分页查询应收fab仓库")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findFabWarehouseByPage")
    public CommonResult<CommonPageResult<FabWarehouseVO>> findFabWarehouseByPage(@RequestBody QueryFabWarehouseForm form) {
        IPage<FabWarehouseVO> pageList = fabWarehouseService.findFabWarehouseByPage(form);
        CommonPageResult<FabWarehouseVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询应收fab仓库list(目的仓库)")
    @ApiOperationSupport(order = 2)
    @PostMapping("/findfabWarehouse")
    public CommonResult<List<FabWarehouseVO>> findfabWarehouse(@RequestBody FabWarehouseArgsForm form) {
        List<FabWarehouseVO> list = fabWarehouseService.findfabWarehouse(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "编辑-保存应收fab仓库")
    @ApiOperationSupport(order = 3)
    @PostMapping("/saveFabWarehouse")
    public CommonResult saveFabWarehouse(@Valid @RequestBody FabWarehouseForm form) {
        return fabWarehouseService.saveFabWarehouse(form);
    }

    @ApiOperation(value = "删除应收fab仓库")
    @ApiOperationSupport(order = 4)
    @PostMapping("/deleteFabWarehouse")
    public CommonResult deleteFabWarehouse(@Valid @RequestBody FabWarehouseParaForm form) {
        return fabWarehouseService.deleteFabWarehouse(form.getId());
    }

    @ApiOperation(value = "审核应收fab仓库")
    @ApiOperationSupport(order = 5)
    @PostMapping("/auditFabWarehouse")
    public CommonResult<CustomerVO> auditFabWarehouse(@Valid @RequestBody FabWarehouseForm form){
        return fabWarehouseService.auditFabWarehouse(form);
    }

}
