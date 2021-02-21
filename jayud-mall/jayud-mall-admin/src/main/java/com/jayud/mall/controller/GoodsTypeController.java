package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.GoodsTypeForm;
import com.jayud.mall.model.vo.GoodsTypeReturnVO;
import com.jayud.mall.model.vo.GoodsTypeVO;
import com.jayud.mall.service.IGoodsTypeService;
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
@RequestMapping("/goodstype")
@Api(tags = "A029-admin-货物类型表接口")
@ApiSort(value = 29)
public class GoodsTypeController {

    @Autowired
    IGoodsTypeService goodsTypeService;

    @ApiOperation(value = "货物类型表List")
    @PostMapping("/findGoodsType")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<GoodsTypeVO>> findGoodsType(@RequestBody GoodsTypeForm form) {
        List<GoodsTypeVO> list = goodsTypeService.findGoodsType(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "货物类型下拉选择,给前端用")
    @PostMapping("/findGoodsTypeBy")
    @ApiOperationSupport(order = 2)
    public CommonResult<GoodsTypeReturnVO> findGoodsTypeBy(){
        GoodsTypeReturnVO goodsTypeReturnVO = goodsTypeService.findGoodsTypeBy();
        return CommonResult.success(goodsTypeReturnVO);
    }


}
