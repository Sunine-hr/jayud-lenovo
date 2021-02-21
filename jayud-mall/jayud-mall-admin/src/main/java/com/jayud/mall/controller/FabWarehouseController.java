package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import com.jayud.mall.model.vo.FabWarehouseVO;
import com.jayud.mall.service.IFabWarehouseService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
