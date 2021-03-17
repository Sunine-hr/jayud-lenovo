package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.AccountParaForm;
import com.jayud.mall.model.bo.QueryAccountReceivableForm;
import com.jayud.mall.model.vo.AccountReceivableVO;
import com.jayud.mall.service.IAccountReceivableService;
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
@RequestMapping("/accountreceivable")
@Api(tags = "A050-admin-应收对账单接口")
@ApiSort(value = 50)
public class AccountReceivableController {

    @Autowired
    IAccountReceivableService accountReceivableService;

    //应收对账单分页查询
    @ApiOperation(value = "应收对账单分页查询")
    @PostMapping("/findAccountReceivableByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<AccountReceivableVO>> findAccountReceivableByPage(
            @RequestBody QueryAccountReceivableForm form) {
        IPage<AccountReceivableVO> pageList = accountReceivableService.findAccountReceivableByPage(form);
        CommonPageResult<AccountReceivableVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //应收对账单-查看明细
    //lookDetail
    @ApiOperation(value = "应收对账单-查看明细")
    @PostMapping("/lookDetail")
    @ApiOperationSupport(order = 2)
    public CommonResult<AccountReceivableVO> lookDetail(@Valid @RequestBody AccountParaForm form){
        Long id = form.getId();
        return accountReceivableService.lookDetail(id);
    }


}
