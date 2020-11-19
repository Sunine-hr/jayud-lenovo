package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryOfferInfoFareForm;
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
@Api(tags = "C端-报价(运价)接口")
public class OfferInfoController {

    @Autowired
    IOfferInfoService offerInfoService;

    @ApiOperation(value = "分页查询报价(运价)")
    @PostMapping("/findOfferInfoFareByPage")
    public CommonResult<CommonPageResult<OfferInfoVO>> findOfferInfoFareByPage(@RequestBody QueryOfferInfoFareForm form) {
        IPage<OfferInfoVO> pageList = offerInfoService.findOfferInfoFareByPage(form);
        CommonPageResult<OfferInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }



}
