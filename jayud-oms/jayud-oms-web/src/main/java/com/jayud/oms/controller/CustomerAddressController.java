package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddCustomerAddressForm;
import com.jayud.oms.model.bo.AddDriverInfoForm;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.bo.QueryDriverInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CustomerAddress;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.model.vo.CustomerAddressVO;
import com.jayud.oms.model.vo.DriverInfoVO;
import com.jayud.oms.service.ICustomerAddressService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 客户地址 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@RestController
@RequestMapping("/customerAddress")
public class CustomerAddressController {

    @Autowired
    private ICustomerAddressService customerAddressService;

    @ApiOperation(value = "分页查询客户地址")
    @PostMapping(value = "/findCustomerAddressByPage")
    public CommonResult<CommonPageResult<DriverInfoVO>> findCustomerAddressByPage(@RequestBody QueryCustomerAddressForm form) {
        IPage<CustomerAddressVO> iPage = customerAddressService.findCustomerAddressByPage(form);
        iPage.getRecords().forEach(CustomerAddressVO::splicingAddress);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增编辑客户地址")
    @PostMapping(value = "/saveOrUpdateCustomerAddress")
    public CommonResult saveOrUpdateCustomerAddress(@Valid @RequestBody AddCustomerAddressForm form) {
        CustomerAddress customerAddress = ConvertUtil.convert(form, CustomerAddress.class);
        if (this.customerAddressService.saveOrUpdateCustomerAddress(customerAddress)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "启用/禁用客户地址,id是客户地址主键")
    @PostMapping(value = "/enableOrDisableCustomerAddress")
    public CommonResult enableOrDisableCustomerAddress(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        CustomerAddress tmp = this.customerAddressService.getById(id);
        String status=StatusEnum.ENABLE.getCode().equals(tmp.getStatus())?StatusEnum.INVALID.getCode():StatusEnum.ENABLE.getCode();
        CustomerAddress customerAddress = new CustomerAddress().setId(id).setStatus(status);
        if (this.customerAddressService.saveOrUpdateCustomerAddress(customerAddress)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取客户地址详情,id是客户地址主键")
    @PostMapping(value = "/getCustomerAddressById")
    public CommonResult getCustomerAddressById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        CustomerAddress customerAddress = this.customerAddressService.getById(id);
        return CommonResult.success(ConvertUtil.convert(customerAddress, CustomerAddressVO.class));
    }

}

