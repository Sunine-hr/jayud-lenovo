package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomerForm;
import com.jayud.mall.model.bo.QueryCustomerForm;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@Api(tags = "后台-客户接口")
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    @ApiOperation(value = "分页查询-客户列表")
    @PostMapping("/findCustomerByPage")
    public CommonResult<CommonPageResult<CustomerVO>> findCustomerByPage(@RequestBody QueryCustomerForm form) {
        IPage<CustomerVO> pageList = customerService.findCustomerByPage(form);
        CommonPageResult<CustomerVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "编辑-保存客户")
    @PostMapping("/saveCustomer")
    public CommonResult saveCustomer(@RequestBody CustomerForm form){
        return customerService.saveCustomer(form);
    }

    @ApiOperation(value = "审核-客户")
    @PostMapping("/auditCustomer")
    public CommonResult<CustomerVO> auditCustomer(@RequestBody CustomerForm form){
        return customerService.auditCustomer(form);
    }

}
