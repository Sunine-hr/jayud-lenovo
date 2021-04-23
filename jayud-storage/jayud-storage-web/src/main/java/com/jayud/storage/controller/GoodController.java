package com.jayud.storage.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.model.bo.GoodForm;
import com.jayud.storage.model.bo.QueryGoodForm;
import com.jayud.storage.model.bo.QueryWarehouseForm;
import com.jayud.storage.model.po.Good;
import com.jayud.storage.model.vo.GoodVO;
import com.jayud.storage.model.vo.WarehouseVO;
import com.jayud.storage.service.IGoodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品信息维护表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/good")
@Api(tags = "商品管理")
public class GoodController {

    @Autowired
    IGoodService goodService;

    @ApiOperation(value = "不分页查询list")
    @PostMapping("/findGoods")
    public CommonResult<List<GoodVO>> findGoods(@RequestBody QueryGoodForm form) {
        List<GoodVO> goodVOS = goodService.getList(form);
        return CommonResult.success(goodVOS);
    }

    @ApiOperation(value = "分页查询list")
    @PostMapping("/findGoodsByPage")
    public CommonResult<IPage<GoodVO>> findGoodsByPage(@RequestBody QueryGoodForm form) {
        IPage<GoodVO> page = goodService.findGoodsByPage(form);
        return CommonResult.success(page);
    }

    @ApiOperation(value = "新增或修改商品信息")
    @PostMapping("/saveOrUpdateGood")
    public CommonResult saveOrUpdateGood(GoodForm goodForm){
        Good convert = ConvertUtil.convert(goodForm, Good.class);
        boolean b = goodService.saveOrUpdate(convert);
        if(b){
            return CommonResult.success();
        }
        return CommonResult.error(444,"添加或修改失败");
    }

    @ApiOperation(value = "校验商品sku是否重复")
    @PostMapping("/isRepeat")
    public CommonResult isRepeat(String sku){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sku",sku);
        Good one = this.goodService.getOne(queryWrapper);
        if(one!=null){
            return CommonResult.error(444,"sku重复，不可提交");
        }
        return CommonResult.success();
    }


}

