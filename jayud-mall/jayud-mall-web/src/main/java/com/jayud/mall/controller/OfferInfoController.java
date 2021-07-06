package com.jayud.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.CustomerMapper;
import com.jayud.mall.model.bo.OfferInfoForm;
import com.jayud.mall.model.bo.QueryOfferInfoFareForm;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.model.vo.FabWarehouseVO;
import com.jayud.mall.model.vo.OfferInfoVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
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
@Api(tags = "C007-client-报价(运价)接口")
@ApiSort(value = 7)
public class OfferInfoController {

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    IOfferInfoService offerInfoService;
    @Autowired
    BaseService baseService;


    @ApiOperation(value = "分页查询报价(运价)")
    @PostMapping("/findOfferInfoFareByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<OfferInfoVO>> findOfferInfoFareByPage(@RequestBody QueryOfferInfoFareForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        Integer customerId = customerUser.getId();
        form.setCustomerId(customerId);
        IPage<OfferInfoVO> pageList = offerInfoService.findOfferInfoFareByPage(form);
        CommonPageResult<OfferInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查看运价服务详情")
    @PostMapping("/lookOfferInfoFare")
    @ApiOperationSupport(order = 2)
    public CommonResult<OfferInfoVO> lookOfferInfoFare(@RequestBody OfferInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        if(ObjectUtil.isEmpty(customerUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "客户失效，请重新登录");
        }
        Long id = form.getId();
        OfferInfoVO offerInfoVO = offerInfoService.lookOfferInfoFare(id);

        //showClearingWay 下单页面显示结算方式
        Integer clearingWay = offerInfoVO.getClearingWay();
        if(ObjectUtil.isNotEmpty(clearingWay) && clearingWay.equals(2)){
            Integer customerId = customerUser.getId();
            CustomerVO customer = customerMapper.findCustomerById(customerId);
            offerInfoVO.setShowClearingWay(customer.getClearingWay());
        }else{
            offerInfoVO.setShowClearingWay(clearingWay);
        }

        return CommonResult.success(offerInfoVO);
    }

    @ApiOperation(value = "去下单-确认订单(查看选择运价)")
    @PostMapping("/purchaseOrders")
    @ApiOperationSupport(order = 3)
    public CommonResult<OfferInfoVO> purchaseOrders(@RequestBody OfferInfoForm form){
        Long id = form.getId();
        OfferInfoVO offerInfoVO = offerInfoService.purchaseOrders(id);
        return CommonResult.success(offerInfoVO);
    }


    //目的地仓库，可达仓库
    @ApiOperation(value = "查询运价，目的地仓库(可达仓库)")
    @PostMapping("/findFabWarehouse")
    @ApiOperationSupport(order = 4)
    public CommonResult<List<FabWarehouseVO>> findFabWarehouse(@RequestBody OfferInfoForm form){
        Long id = form.getId();
        List<FabWarehouseVO> fabWarehouseVOList = offerInfoService.findFabWarehouse(id);
        return CommonResult.success(fabWarehouseVOList);
    }

    @ApiOperation(value = "客户首页(最新报价Top4)")
    @PostMapping("/findOfferInfoFareTop4")
    @ApiOperationSupport(order = 5)
    public CommonResult<List<OfferInfoVO>> findOfferInfoFareTop4(@RequestBody QueryOfferInfoFareForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        Integer customerId = customerUser.getId();
        form.setCustomerId(customerId);
        List<OfferInfoVO> pageList = offerInfoService.findOfferInfoFareTop4(form);
        return CommonResult.success(pageList);
    }


}
