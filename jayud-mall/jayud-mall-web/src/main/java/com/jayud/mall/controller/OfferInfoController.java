package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OfferInfoForm;
import com.jayud.mall.model.bo.QueryOfferInfoFareForm;
import com.jayud.mall.model.vo.FabWarehouseVO;
import com.jayud.mall.model.vo.OfferInfoVO;
import com.jayud.mall.service.IOfferInfoService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offerinfo")
@Api(tags = "C010-C端-报价(运价)接口")
@ApiSort(value = 10)
public class OfferInfoController {

    @Autowired
    IOfferInfoService offerInfoService;

    @ApiOperation(value = "分页查询报价(运价)")
    @PostMapping("/findOfferInfoFareByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<OfferInfoVO>> findOfferInfoFareByPage(@RequestBody QueryOfferInfoFareForm form) {
        IPage<OfferInfoVO> pageList = offerInfoService.findOfferInfoFareByPage(form);
        CommonPageResult<OfferInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查看运价服务详情")
    @PostMapping("/lookOfferInfoFare")
    @ApiOperationSupport(order = 2)
    public CommonResult<OfferInfoVO> lookOfferInfoFare(@RequestBody OfferInfoForm form) {
        Long id = form.getId();
        OfferInfoVO offerInfoVO = offerInfoService.lookOfferInfoFare(id);
        return CommonResult.success(offerInfoVO);
    }

    @ApiModelProperty(value = "去下单-确认订单(查看选择运价)")
    @PostMapping("/purchaseOrders")
    @ApiOperationSupport(order = 3)
    public CommonResult<OfferInfoVO> purchaseOrders(@RequestBody OfferInfoForm form){
        Long id = form.getId();
        OfferInfoVO offerInfoVO = offerInfoService.purchaseOrders(id);
        return CommonResult.success(offerInfoVO);
    }


    //目的地仓库，可达仓库
    @ApiModelProperty(value = "查询运价，目的地仓库(可达仓库)")
    @PostMapping("/findFabWarehouse")
    @ApiOperationSupport(order = 4)
    public CommonResult<List<FabWarehouseVO>> findFabWarehouse(@RequestBody OfferInfoForm form){
        Long id = form.getId();
        List<FabWarehouseVO> fabWarehouseVOList = offerInfoService.findFabWarehouse(id);
        return CommonResult.success(fabWarehouseVOList);
    }



}
