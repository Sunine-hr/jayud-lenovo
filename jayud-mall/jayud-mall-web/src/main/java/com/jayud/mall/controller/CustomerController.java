package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomerPwdForm;
import com.jayud.mall.model.bo.CustomerRegisterForm;
import com.jayud.mall.model.bo.CustomerVerifyForm;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.service.ICustomerService;
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
@RequestMapping("/customer")
@Api(tags = "C002-C端-客户接口")
@ApiSort(value = 2)
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    @ApiOperation(value = "客户注册")
    @PostMapping("/customerRegister")
    @ApiOperationSupport(order = 1)
    public CommonResult<CustomerVO> customerRegister(@Valid @RequestBody CustomerRegisterForm form){
        return customerService.customerRegister(form);
    }

    @ApiOperation(value = "忘记密码-客户验证")
    @PostMapping("/customerVerify")
    @ApiOperationSupport(order = 2)
    public CommonResult customerVerify(@Valid @RequestBody CustomerVerifyForm form){
        return customerService.customerVerify(form);
    }

    @ApiOperation(value = "修改密码-客户确认密码")
    @PostMapping("/customerUpdatePwd")
    @ApiOperationSupport(order = 3)
    public CommonResult customerUpdatePwd(@Valid @RequestBody CustomerPwdForm form){
        return customerService.customerUpdatePwd(form);
    }

}
