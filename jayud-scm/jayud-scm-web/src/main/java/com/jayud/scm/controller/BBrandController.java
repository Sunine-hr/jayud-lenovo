package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommodityForm;
import com.jayud.scm.model.po.BBrand;
import com.jayud.scm.service.IBBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 品牌库表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/bBrand")
@Api(tags = "品牌库管理")
public class BBrandController {

    @Autowired
    private IBBrandService brandService;

    @ApiOperation(value = "根据品牌名获取品牌全名")
    @PostMapping(value = "/getNameByNameEn")
    public CommonResult getNameByNameEn(@RequestBody Map<String,Object> map) {
        String skuBrand = MapUtil.getStr(map,"skuBrand");
        BBrand bBrand = brandService.getNameByNameEn(skuBrand);
        if(bBrand != null){
            return CommonResult.success(bBrand.getAllName());
        }
        return CommonResult.success(skuBrand);
    }

}

