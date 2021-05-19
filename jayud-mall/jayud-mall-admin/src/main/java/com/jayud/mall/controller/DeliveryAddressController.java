package com.jayud.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.model.bo.QueryDeliveryAddressForm;
import com.jayud.mall.model.vo.DeliveryAddressVO;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/deliveryaddress")
@Api(tags = "A061-admin-地址接口(提货、收货)")
@ApiSort(value = 61)
public class DeliveryAddressController {

    @Autowired
    IDeliveryAddressService deliveryAddressService;
    @Autowired
    BaseService baseService;


    @ApiOperation(value = "分页查询-地址(提货、收货)")
    @PostMapping("/findDeliveryAddressByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<DeliveryAddressVO>> findDeliveryAddressByPage(@Valid @RequestBody QueryDeliveryAddressForm form) {
        Integer customerId = form.getCustomerId();
        if(ObjectUtil.isEmpty(customerId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "客户id不能为空");
        }
        IPage<DeliveryAddressVO> pageList = deliveryAddressService.findDeliveryAddressByPage(form);
        CommonPageResult<DeliveryAddressVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

}
