package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.FabWarehouseArgsForm;
import com.jayud.mall.model.bo.FabWarehouseWarehouseCodeForm;
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

import java.util.List;

@RestController
@RequestMapping("/fabwarehouse")
@Api(tags = "C010-client-应收FBA仓库接口(目的仓库)")
@ApiSort(value = 10)
public class FabWarehouseController {

    @Autowired
    IFabWarehouseService fabWarehouseService;

    @ApiOperation(value = "查询应收FBA仓库list(目的仓库)")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findfabWarehouse")
    public CommonResult<List<FabWarehouseVO>> findfabWarehouse(@RequestBody FabWarehouseArgsForm form) {
        List<FabWarehouseVO> list = fabWarehouseService.findfabWarehouse(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "根据代码，查询应收FBA仓库(目的仓库)")
    @ApiOperationSupport(order = 2)
    @PostMapping("/findfabWarehouseByWarehouseCode")
    public CommonResult<FabWarehouseVO> findfabWarehouseByWarehouseCode(@RequestBody FabWarehouseWarehouseCodeForm form) {
        String warehouseCode = form.getWarehouseCode();
        FabWarehouseVO fabWarehouseVO = fabWarehouseService.findfabWarehouseByWarehouseCode(warehouseCode);
        return CommonResult.success(fabWarehouseVO);
    }


}
