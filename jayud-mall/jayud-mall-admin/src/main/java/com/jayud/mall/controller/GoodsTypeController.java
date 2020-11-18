package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.GoodsTypeForm;
import com.jayud.mall.model.bo.HarbourInfoForm;
import com.jayud.mall.model.po.GoodsType;
import com.jayud.mall.model.po.HarbourInfo;
import com.jayud.mall.service.IGoodsTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goodstype")
@Api(tags = "(报价&货物)类型表接口")
public class GoodsTypeController {

    @Autowired
    IGoodsTypeService goodsTypeService;

    @ApiOperation(value = "(报价&货物)类型表List")
    @PostMapping("/findGoodsType")
    public CommonResult<List<GoodsType>> findGoodsType(@RequestBody GoodsTypeForm form) {
        List<GoodsType> list = goodsTypeService.findGoodsType(form);
        return CommonResult.success(list);
    }


}
