package com.jayud.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.bo.CreatePaymentBillForm;
import com.jayud.finance.bo.QueryNotPaidBillForm;
import com.jayud.finance.bo.QueryPaymentBillForm;
import com.jayud.finance.bo.QueryPaymentBillNumForm;
import com.jayud.finance.service.IOrderPaymentBillService;
import com.jayud.finance.vo.OrderPaymentBillVO;
import com.jayud.finance.vo.PaymentNotPaidBillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;


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

    @ApiOperation(value = "账单数列表")
    @PostMapping("/findPaymentBillNum")
    public CommonResult<Map<String,Object>> findPaymentBillNum(@RequestBody @Valid QueryPaymentBillNumForm form) {
        Map<String,Object> result = billService.findPaymentBillNum(form);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "未出账订单数列表")
    @PostMapping("/findNotPaidBillByPage")
    public CommonResult<CommonPageResult<PaymentNotPaidBillVO>> findNotPaidBillByPage(@RequestBody QueryNotPaidBillForm form) {
        IPage<PaymentNotPaidBillVO> pageList = billService.findNotPaidBillByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "暂存和生成应付账单")
    @PostMapping("/createBill")
    public CommonResult createPaymentBill(@RequestBody @Valid CreatePaymentBillForm form) {
        Boolean result = billService.createPaymentBill(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.OPR_FAIL.getCode(),ResultEnum.OPR_FAIL.getMessage());
    }
}
