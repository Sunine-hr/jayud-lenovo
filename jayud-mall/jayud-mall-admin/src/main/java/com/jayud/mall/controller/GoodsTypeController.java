package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.GoodsTypeForm;
import com.jayud.mall.model.po.GoodsType;
import com.jayud.mall.model.vo.GoodsTypeReturnVO;
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
@Api(tags = "货物类型表接口")
public class GoodsTypeController {

    @Autowired
    IGoodsTypeService goodsTypeService;

    @ApiOperation(value = "货物类型表List")
    @PostMapping("/findGoodsType")
    public CommonResult<List<GoodsType>> findGoodsType(@RequestBody GoodsTypeForm form) {
        List<GoodsType> list = goodsTypeService.findGoodsType(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "货物类型下拉选择")
    @PostMapping("/findGoodsTypeBy")
    public CommonResult<GoodsTypeReturnVO> findGoodsTypeBy(){
        GoodsTypeReturnVO goodsTypeReturnVO = goodsTypeService.findGoodsTypeBy();
        return CommonResult.success(goodsTypeReturnVO);
    }


}
