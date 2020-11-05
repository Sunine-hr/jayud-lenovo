package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OfferInfoForm;
import com.jayud.mall.model.bo.QueryOfferInfoForm;
import com.jayud.mall.model.vo.OfferInfoVO;
import com.jayud.mall.service.IOfferInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offerinfo")
@Api(tags = "报价接口")
public class OfferInfoController {

    @Autowired
    IOfferInfoService offerInfoService;

    @ApiOperation(value = "分页查询报价")
    @PostMapping("/findOfferInfoByPage")
    public CommonResult<CommonPageResult<OfferInfoVO>> findOfferInfoByPage(@RequestBody QueryOfferInfoForm form) {
        IPage<OfferInfoVO> pageList = offerInfoService.findOfferInfoByPage(form);
        CommonPageResult<OfferInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "禁用报价")
    @PostMapping(value = "/disabledOfferInfo")
    public CommonResult disabledOfferInfo(@RequestBody OfferInfoForm form) {
        Long id = form.getId();
        offerInfoService.disabledOfferInfo(id);
        return CommonResult.success("禁用报价,成功！");
    }

    @ApiOperation(value = "启用报价")
    @PostMapping(value = "/enableOfferInfo")
    public CommonResult enableOfferInfo(@RequestBody OfferInfoForm form) {
        Long id = form.getId();
        offerInfoService.enableOfferInfo(id);
        return CommonResult.success("启用报价,成功！");
    }

    @ApiOperation(value = "添加报价")
    @PostMapping(value = "saveOfferInfo")
    public CommonResult saveOfferInfo(@RequestBody OfferInfoForm form){
        offerInfoService.saveOfferInfo(form);
        return CommonResult.success("保存报价，成功！");
    }

    @ApiOperation(value = "查看报价详情")
    @PostMapping(value = "lookOfferInfo")
    public CommonResult<OfferInfoVO> lookOfferInfo(@RequestBody OfferInfoForm form){
        Long id = form.getId();
        return offerInfoService.lookOfferInfo(id);
    }



}
