package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import com.jayud.mall.model.bo.QueryShippingAreaForm;
import com.jayud.mall.model.po.FabWarehouse;
import com.jayud.mall.model.po.ShippingArea;
import com.jayud.mall.service.IShippingAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shippingarea")
@Api(tags = "集货仓表接口")
public class ShippingAreaController {

    @Autowired
    IShippingAreaService shippingAreaService;

    @ApiOperation(value = "分页查询集货仓表")
    @PostMapping("/findShippingAreaByPage")
    public CommonResult<CommonPageResult<ShippingArea>> findShippingAreaByPage(@RequestBody QueryShippingAreaForm form) {
        IPage<ShippingArea> pageList = shippingAreaService.findShippingAreaByPage(form);
        CommonPageResult<ShippingArea> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

}
