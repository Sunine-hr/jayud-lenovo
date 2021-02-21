package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryShippingAreaForm;
import com.jayud.mall.model.vo.ShippingAreaVO;
import com.jayud.mall.service.IShippingAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shippingarea")
@Api(tags = "A030-admin-集货仓表接口")
@ApiSort(value = 30)
public class ShippingAreaController {

    @Autowired
    IShippingAreaService shippingAreaService;

    @ApiOperation(value = "分页查询集货仓表")
    @PostMapping("/findShippingAreaByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<ShippingAreaVO>> findShippingAreaByPage(@RequestBody QueryShippingAreaForm form) {
        IPage<ShippingAreaVO> pageList = shippingAreaService.findShippingAreaByPage(form);
        CommonPageResult<ShippingAreaVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

}
