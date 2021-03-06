package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.DeliveryAddressForm;
import com.jayud.mall.model.bo.QueryDeliveryAddressForm;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IDeliveryAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliveryaddress")
@Api(tags = "C005-client-地址接口(提货、收货)")
@ApiSort(value = 5)
public class DeliveryAddressController {

    @Autowired
    IDeliveryAddressService deliveryAddressService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "分页查询-地址(提货、收货)")
    @PostMapping("/findDeliveryAddressByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<DeliveryAddressVO>> findDeliveryAddressByPage(@RequestBody QueryDeliveryAddressForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        Integer customerId = customerUser.getId();
        form.setCustomerId(customerId);
        IPage<DeliveryAddressVO> pageList = deliveryAddressService.findDeliveryAddressByPage(form);
        CommonPageResult<DeliveryAddressVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存-地址(提货、收货)")
    @PostMapping("/saveDeliveryAddress")
    @ApiOperationSupport(order = 2)
    public CommonResult<DeliveryAddressVO> saveDeliveryAddress(@RequestBody DeliveryAddressForm form){
        return deliveryAddressService.saveDeliveryAddress(form);
    }


}
