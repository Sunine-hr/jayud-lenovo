package com.jayud.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.model.bo.CustomerAuditForm;
import com.jayud.mall.model.bo.CustomerEditForm;
import com.jayud.mall.model.bo.CustomerParaForm;
import com.jayud.mall.model.bo.QueryCustomerForm;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.model.vo.domain.AuthUser;
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
import java.util.List;

@RestController
@RequestMapping("/customer")
@Api(tags = "A017-admin-客户接口")
@ApiSort(value = 17)
public class CustomerController {

    @Autowired
    ICustomerService customerService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "分页查询-客户列表")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findCustomerByPage")
    public CommonResult<CommonPageResult<CustomerVO>> findCustomerByPage(@RequestBody QueryCustomerForm form) {
        IPage<CustomerVO> pageList = customerService.findCustomerByPage(form);
        CommonPageResult<CustomerVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "权限用户查询-客户列表")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findAuthCustomerByPage")
    public CommonResult<CommonPageResult<CustomerVO>> findAuthCustomerByPage(@RequestBody QueryCustomerForm form) {
        AuthUser user = baseService.getUser();
        if(ObjectUtil.isEmpty(user)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户过期，请重新登录");
        }
        form.setMemberUserId(user.getId());
        IPage<CustomerVO> pageList = customerService.findAuthCustomerByPage(form);
        CommonPageResult<CustomerVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "编辑-保存客户")
    @ApiOperationSupport(order = 2)
    @PostMapping("/saveCustomer")
    public CommonResult saveCustomer(@RequestBody CustomerEditForm form){
        return customerService.saveCustomer(form);
    }

    @ApiOperation(value = "审核-客户")
    @ApiOperationSupport(order = 3)
    @PostMapping("/auditCustomer")
    public CommonResult<CustomerVO> auditCustomer(@RequestBody CustomerAuditForm form){
        return customerService.auditCustomer(form);
    }

    @ApiOperation(value = "查询客户list")
    @ApiOperationSupport(order = 4)
    @PostMapping("/findCustomer")
    public CommonResult<List<CustomerVO>> findCustomer() {
        List<CustomerVO> list = customerService.findCustomer();
        return CommonResult.success(list);
    }

    //重置密码-随机8位数(字母+@&+数字)
    @ApiOperation(value = "重置密码-随机8位数(字母+@&+数字)")
    @ApiOperationSupport(order = 5)
    @PostMapping("/resetPasswords")
    public CommonResult<String> resetPasswords(@Valid @RequestBody CustomerParaForm form){
        String random = customerService.resetPasswords(form);
        return CommonResult.success(random);
    }



}
