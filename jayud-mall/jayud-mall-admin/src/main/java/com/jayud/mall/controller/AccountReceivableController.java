package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.AccountParaForm;
import com.jayud.mall.model.bo.BillMasterForm;
import com.jayud.mall.model.bo.MonthlyStatementForm;
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

    //TODO 定时任务 调用创建应收对账单
    /**
     * 生成月结账单(创建应收对账单)
     * 1.月结，整个系统的月份的应收、应付账单
     * 2.根据 法人主体、客户名称、账单日期(月份) 分组查询，创建应收对账单
     * 3.创建月结账单 monthlyStatement
     */
    @ApiOperation(value = "生成应收月结账单(创建应收对账单)")
    @PostMapping("/createRecMonthlyStatement")
    @ApiOperationSupport(order = 3)
    public CommonResult createRecMonthlyStatement(@Valid @RequestBody MonthlyStatementForm form){
        return accountReceivableService.createRecMonthlyStatement(form);
    }

    //TODO 应收对账单下应收账单核销
    @ApiOperation(value = "核销账单(应收账单)")
    @PostMapping("/verificationBill")
    @ApiOperationSupport(order = 4)
    public CommonResult verificationBill(@Valid @RequestBody BillMasterForm form){
        Long id = form.getId();//应收账单主单 receivable_bill_master id
        return accountReceivableService.verificationBill(id);
    }




}
