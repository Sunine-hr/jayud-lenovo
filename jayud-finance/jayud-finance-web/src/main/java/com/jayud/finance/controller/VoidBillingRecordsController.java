package com.jayud.finance.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.finance.bo.QueryReceiveBillForm;
import com.jayud.finance.bo.QueryVoidBillingForm;
import com.jayud.finance.bo.VoidBillForm;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.po.VoidBillingRecords;
import com.jayud.finance.service.IVoidBillingRecordsService;
import com.jayud.finance.vo.OrderReceiveBillVO;
import com.jayud.finance.vo.VoidBillingRecordsVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 作废账单表 前端控制器
 * </p>
 *
 * @author chuanmei
 * @since 2021-06-21
 */
@RestController
@RequestMapping("/voidBilling")
public class VoidBillingRecordsController {

    @Autowired
    private IVoidBillingRecordsService voidBillingRecordsService;


    /**
     * 作废账单
     */
    @ApiOperation(value = "作废账单")
    @PostMapping("/voidBill")
    public CommonResult voidBill(@RequestBody @Valid VoidBillForm form) {
        form.checkVoidBill();
        this.voidBillingRecordsService.voidBill(form.getBillNo(), form.getCostType(),
                Integer.valueOf(OrderStatusEnum.COST_3.getCode()));
        return CommonResult.success();
    }

    /**
     * 作废账单
     */
    @ApiOperation(value = "作废账单")
    @PostMapping("/findVoidBillByPage")
    public CommonResult<CommonPageResult<VoidBillingRecordsVO>> findVoidBillByPage(@RequestBody @Valid QueryVoidBillingForm form) {
        IPage<VoidBillingRecordsVO> list = this.voidBillingRecordsService.findVoidBillByPage(form);
        return CommonResult.success(new CommonPageResult<>(list));
    }
}

