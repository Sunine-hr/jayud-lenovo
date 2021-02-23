package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
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
@Api(tags = "C004-client-客户接口")
@ApiSort(value = 4)
public class CustomerController {

    @Autowired
    ICustomerService customerService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "客户注册(注册保存)")
    @PostMapping("/customerRegister")
    @ApiOperationSupport(order = 1)
    public CommonResult<CustomerVO> customerRegister(@Valid @RequestBody CustomerRegisterForm form){
        return customerService.customerRegister(form);
    }

    @ApiOperation(value = "忘记密码-客户验证(确认)")
    @PostMapping("/customerVerify")
    @ApiOperationSupport(order = 2)
    public CommonResult customerVerify(@Valid @RequestBody CustomerVerifyForm form){
        return customerService.customerVerify(form);
    }

    @ApiOperation(value = "修改密码-客户新密码(确认)")
    @PostMapping("/customerUpdatePwd")
    @ApiOperationSupport(order = 3)
    public CommonResult customerUpdatePwd(@Valid @RequestBody CustomerPwdForm form){
        return customerService.customerUpdatePwd(form);
    }

    @ApiOperation(value = "个人中心-我的账号")
    @PostMapping("/findMyAccount")
    @ApiOperationSupport(order = 4)
    public CommonResult<CustomerVO> findMyAccount(){
        CustomerUser customerUser = baseService.getCustomerUser();
        Integer id = customerUser.getId();
        return customerService.findCustomerById(id);
    }

    @ApiOperation(value = "我的账号-账号编辑(保存)")
    @PostMapping("/updateCustomer")
    @ApiOperationSupport(order = 5)
    public CommonResult<CustomerVO> updateCustomer(@Valid @RequestBody CustomerEditForm form){
        return customerService.saveCustomer(form);
    }

    @ApiOperation(value = "我的账号-更换手机-验证身份(点击下一步时触发)")
    @PostMapping("/customerUpdatePhoneVerify")
    @ApiOperationSupport(order = 6)
    public CommonResult customerUpdatePhoneVerify(@Valid @RequestBody CustomerVerifyForm form){
        return customerService.customerVerify(form);
    }

    @ApiOperation(value = "我的账号-更换手机-设置新手机号")
    @PostMapping("/customerUpdatePhone")
    @ApiOperationSupport(order = 7)
    public CommonResult<CustomerVO> customerUpdatePhone(@Valid @RequestBody CustomerPhoneForm form){
        return customerService.customerUpdatePhone(form);
    }

    @ApiOperation(value = "客户登录(已废弃)")
    @PostMapping("/customerLogin")
    @Deprecated
    @ApiOperationSupport(order = 8)
    public CommonResult<CustomerVO> customerLogin(@Valid @RequestBody CustomerLoginForm form){
        CustomerVO customerVO = customerService.customerLogin(form);
        return CommonResult.success(customerVO);
    }

}
