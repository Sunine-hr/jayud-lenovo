package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomerRegisterForm;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    public CommonResult<CustomerVO> customerRegister(@Valid @RequestBody CustomerRegisterForm form){
        return customerService.customerRegister(form);
    }

}
