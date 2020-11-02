package com.jayud.tms.controller;


import com.jayud.common.CommonResult;
import com.jayud.tms.model.bo.QueryDeliveryAddressForm;
import com.jayud.tms.model.vo.DeliveryAddressVO;
import com.jayud.tms.service.IDeliveryAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/common")
@Api(tags = "中港运输通用接口")
public class OrderTransCommonController {

    @Autowired
    IDeliveryAddressService deliveryAddressService;

    @ApiOperation(value = "获取提货/送货地址")
    @PostMapping(value = "/findDeliveryAddress")
    public CommonResult<List<DeliveryAddressVO>> findDeliveryAddress(@RequestBody QueryDeliveryAddressForm form) {
        List<DeliveryAddressVO> addressVOS = deliveryAddressService.findDeliveryAddress(form);
        return CommonResult.success(addressVOS);
    }






}

