package com.jayud.storage.controller;


import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.OmsClient;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
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

    @Autowired
    private OmsClient omsClient;

    @ApiOperation(value = "不分页查询list")
    @PostMapping("/findGoods")
    public CommonResult<List<GoodVO>> findGoods(@RequestBody QueryGoodForm form) {
        List<GoodVO> goodVOS = goodService.getList(form);
        return CommonResult.success(goodVOS);
    }

    @ApiOperation(value = "分页查询list")
    @PostMapping("/findGoodsByPage")
    public CommonResult<IPage<GoodVO>> findGoodsByPage(@RequestBody QueryGoodForm form) {

        if (!StringUtils.isEmpty(form.getCustomerName())) {
            ApiResult result = omsClient.getCustomerIdByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null && ((List) data).size() > 0) {
                JSONArray mainOrders = new JSONArray(data);
                form.assemblyCustomerIds(mainOrders);
            } else {
                form.setCustomerIds(Collections.singletonList((long)-1));
            }
        }

        IPage<GoodVO> page = goodService.findGoodsByPage(form);
        for (GoodVO record : page.getRecords()) {
            record.setCustomerName(omsClient.getCustomerNameById(record.getCustomerId()).getData());
            record.setProductSize();
        }
        return CommonResult.success(page);
    }

    @ApiOperation(value = "新增或修改商品信息")
    @PostMapping("/saveOrUpdateGood")
    public CommonResult saveOrUpdateGood(@RequestBody GoodForm goodForm){
        Good convert = ConvertUtil.convert(goodForm, Good.class);
        if(convert.getId() == null){
            convert.setSku(convert.getCustomerCode()+"-"+convert.getSku());
            if(this.isRepeat(convert.getSku()).getCode().equals(444)){
                return CommonResult.error(444,"sku重复，不可提交");
            }
        }
        convert.setStatus(1);
        convert.setCreateTime(LocalDateTime.now());
        convert.setCreateUser(UserOperator.getToken());
        boolean b = goodService.saveOrUpdate(convert);
        if(b){
            return CommonResult.success();
        }
        return CommonResult.error(443,"添加或修改失败");
    }

    @ApiOperation(value = "校验商品sku是否重复")
    @PostMapping("/isRepeat")
    public CommonResult isRepeat(@RequestParam("sku") String sku){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sku",sku);
        Good one = this.goodService.getOne(queryWrapper);
        if(one!=null){
            return CommonResult.error(444,"sku重复，请修改");
        }
        return CommonResult.success();
    }


}

