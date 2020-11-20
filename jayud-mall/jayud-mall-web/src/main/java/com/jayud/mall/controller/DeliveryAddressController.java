package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.DeliveryAddressForm;
import com.jayud.mall.model.bo.QueryDeliveryAddressForm;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.service.IDeliveryAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliveryaddress")
@Api(tags = "C端-提货、收货地址基础数据表接口")
public class DeliveryAddressController {

    @Autowired
    IDeliveryAddressService deliveryAddressService;

    @ApiOperation(value = "分页查询-提货、收货地址")
    @PostMapping("/findDeliveryAddressByPage")
    public CommonResult<CommonPageResult<DeliveryAddressVO>> findDeliveryAddressByPage(@RequestBody QueryDeliveryAddressForm form) {
        IPage<DeliveryAddressVO> pageList = deliveryAddressService.findDeliveryAddressByPage(form);
        CommonPageResult<DeliveryAddressVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存-提货、收货地址")
    @PostMapping("/saveDeliveryAddress")
    public CommonResult<DeliveryAddressVO> saveDeliveryAddress(@RequestBody DeliveryAddressForm form){
        return deliveryAddressService.saveDeliveryAddress(form);
    }


}
