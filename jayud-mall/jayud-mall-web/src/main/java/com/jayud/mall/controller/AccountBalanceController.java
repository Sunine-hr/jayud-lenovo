package com.jayud.mall.controller;


import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.AccountBalanceForm;
import com.jayud.mall.model.vo.AccountBalanceVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IAccountBalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accountbalance")
@Api(tags = "C014-client-账户余额接口")
@ApiSort(value = 14)
public class AccountBalanceController {

    @Autowired
    IAccountBalanceService accountBalanceService;
    @Autowired
    BaseService baseService;

    //web端查询当前登录账户余额
    @ApiOperation(value = "web端查询当前登录账户余额(前端不需要传参数)")
    @PostMapping("/findCurrAccountBalance")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<AccountBalanceVO>> findCurrAccountBalance(@RequestBody AccountBalanceForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(Long.valueOf(customerUser.getId()));//当前登录客户
        List<AccountBalanceVO> list = accountBalanceService.findCurrAccountBalance(form);
        return CommonResult.success(list);
    }


}
