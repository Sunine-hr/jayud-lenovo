package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddAccountBankBillEntryForm;
import com.jayud.scm.model.bo.AddAccountBankBillEntryForm2;
import com.jayud.scm.model.bo.AddCheckOrderEntryForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.po.AccountBankBill;
import com.jayud.scm.model.po.AccountBankBillEntry;
import com.jayud.scm.service.IAccountBankBillEntryService;
import com.jayud.scm.service.IAccountBankBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 水单明细表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@RestController
@RequestMapping("/accountBankBillEntry")
@Api(tags = "水单明细管理")
public class AccountBankBillEntryController {

    @Autowired
    private IAccountBankBillEntryService accountBankBillEntryService;

    @Autowired
    private IAccountBankBillService accountBankBillService;

    @ApiOperation(value = "新增水单明细")
    @PostMapping(value = "/addCheckOrderEntry")
    public CommonResult addAccountBankBillEntry(@RequestBody AddAccountBankBillEntryForm2 form) {

        if(CollectionUtils.isEmpty(form.getAddAccountBankBillEntryForms())){
            return CommonResult.error(444,"新增数据不能为空");
        }

        List<AccountBankBillEntry> listByAccountBankBillId = accountBankBillEntryService.getListByAccountBankBillId(form.getBankBillId().longValue());

        BigDecimal total = new BigDecimal(0);
        if(CollectionUtils.isNotEmpty(listByAccountBankBillId)){
            for (AccountBankBillEntry accountBankBillEntry : listByAccountBankBillId) {
                total = total.add(accountBankBillEntry.getAcMoney());
            }
        }
        for (AddAccountBankBillEntryForm addAccountBankBillEntryForm : form.getAddAccountBankBillEntryForms()) {
            total = total.add(addAccountBankBillEntryForm.getAcMoney());
        }
        AccountBankBill bankBill = accountBankBillService.getById(form.getBankBillId());
        if(bankBill.getVerificationMoney() == null || bankBill.getVerificationMoney().compareTo(total) == -1){
            return CommonResult.error(444,"水单明细总金额不能大于水单主表金额");
        }


        boolean result = accountBankBillEntryService.addAccountBankBillEntry(form);
        if(!result){
            return CommonResult.error(444,"新增水单明细失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "删除水单明细")
    @PostMapping(value = "/deleteAccountBankBillEntry")
    public CommonResult deleteAccountBankBillEntry(@RequestBody DeleteForm form) {
        boolean result = accountBankBillEntryService.deleteAccountBankBillEntry(form);
        if(!result){
            return CommonResult.error(444,"删除水单明细失败");
        }
        return CommonResult.success();
    }

}

