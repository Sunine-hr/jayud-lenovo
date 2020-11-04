package com.jayud.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.QueryPaymentBillForm;
import com.jayud.finance.service.IOrderPaymentBillService;
import com.jayud.finance.vo.OrderPaymentBillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/paymentBill/")
@Api(tags = "应付出账")
public class PaymentBillController {

    @Autowired
    IOrderPaymentBillService billService;

    @ApiOperation(value = "应付出账单列表")
    @PostMapping("/findPaymentBillByPage")
    public CommonResult<CommonPageResult<OrderPaymentBillVO>> findPaymentBillByPage(@RequestBody QueryPaymentBillForm form) {
        IPage<OrderPaymentBillVO> pageList = billService.findPaymentBillByPage(form);
        CommonPageResult<OrderPaymentBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

}
