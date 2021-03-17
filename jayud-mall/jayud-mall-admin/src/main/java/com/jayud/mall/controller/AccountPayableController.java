package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.AccountParaForm;
import com.jayud.mall.model.bo.QueryAccountPayableForm;
import com.jayud.mall.model.vo.AccountPayableVO;
import com.jayud.mall.service.IAccountPayableService;
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
@RequestMapping("/accountpayable")
@Api(tags = "A051-admin-应付对账单接口")
@ApiSort(value = 51)
public class AccountPayableController {

    @Autowired
    IAccountPayableService accountPayableService;

    @ApiOperation(value = "应付对账单分页查询")
    @PostMapping("/findAccountPayableByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<AccountPayableVO>> findAccountPayableByPage(
            @RequestBody QueryAccountPayableForm form) {
        IPage<AccountPayableVO> pageList = accountPayableService.findAccountPayableByPage(form);
        CommonPageResult<AccountPayableVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "应付对账单-查看明细")
    @PostMapping("/lookDetail")
    @ApiOperationSupport(order = 2)
    public CommonResult<AccountPayableVO> lookDetail(@Valid @RequestBody AccountParaForm form){
        Long id = form.getId();
        return accountPayableService.lookDetail(id);
    }



}
