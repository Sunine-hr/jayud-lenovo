package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import com.jayud.mall.model.po.FabWarehouse;
import com.jayud.mall.service.IFabWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fabwarehouse")
@Api(tags = "应收/FBA仓库接口")
public class FabWarehouseController {

    @Autowired
    IFabWarehouseService fabWarehouseService;

    @ApiOperation(value = "分页查询应收/FBA仓库")
    @PostMapping("/findFabWarehouseByPage")
    public CommonResult<CommonPageResult<FabWarehouse>> findFabWarehouseByPage(@RequestBody QueryFabWarehouseForm form) {
        IPage<FabWarehouse> pageList = fabWarehouseService.findFabWarehouseByPage(form);
        CommonPageResult<FabWarehouse> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


}
