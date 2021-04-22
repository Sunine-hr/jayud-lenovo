package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.AccountParaForm;
import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.model.bo.PaymentBillForm;
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

    //TODO 定时任务 调用创建应付对账单
    /**
     * 生成月结账单(创建应付对账单)
     * 1.月结，整个系统的月份的应收、应付账单
     * 2.根据 法人主体、供应商、账单日期(月份) 分组查询，创建应收对账单
     * 3.创建月结账单 monthlyStatement
     */
    @ApiOperation(value = "生成应付月结账单(创建应付对账单)")
    @PostMapping("/createPayMonthlyStatement")
    @ApiOperationSupport(order = 3)
    public CommonResult createPayMonthlyStatement(@Valid @RequestBody MonthlyStatementForm form){
        return accountPayableService.createPayMonthlyStatement(form);
    }


    //TODO 应付对账单下应付账单付款
    @ApiOperation(value = "付款账单(应付账单)-付款")
    @PostMapping("/paymentBill")
    @ApiOperationSupport(order = 4)
    public CommonResult paymentBill(@Valid @RequestBody PaymentBillForm form){
        return accountPayableService.paymentBill(form);
    }



}
