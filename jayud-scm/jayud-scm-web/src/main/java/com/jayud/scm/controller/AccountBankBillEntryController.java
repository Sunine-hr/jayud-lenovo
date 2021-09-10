package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddAccountBankBillEntryForm;
import com.jayud.scm.model.bo.AddCheckOrderEntryForm;
import com.jayud.scm.model.po.AccountBankBillEntry;
import com.jayud.scm.service.IAccountBankBillEntryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "新增提货验货单明细")
    @PostMapping(value = "/addCheckOrderEntry")
    public CommonResult addAccountBankBillEntry(@RequestBody List<AddAccountBankBillEntryForm> form) {

        if(CollectionUtils.isEmpty(form)){
            return CommonResult.error(444,"新增数据不能为空");
        }

//        List<AccountBankBillEntry> listByAccountBankBillId = accountBankBillEntryService.getListByAccountBankBillId(form.get(0).getBankBillId().longValue());
//
//        if(CollectionUtils.isNotEmpty(listByAccountBankBillId)){
//
//        }
//        for (AddAccountBankBillEntryForm addAccountBankBillEntryForm : form) {
//
//        }

        boolean result = accountBankBillEntryService.addAccountBankBillEntry(form);
        if(!result){
            return CommonResult.error(444,"新增水单明细失败");
        }
        return CommonResult.success();
    }

}

