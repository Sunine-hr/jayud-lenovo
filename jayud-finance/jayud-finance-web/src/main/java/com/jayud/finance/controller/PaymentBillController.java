package com.jayud.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.service.IOrderPaymentBillService;
import com.jayud.finance.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/paymentBill/")
@Api(tags = "应付出账")
public class PaymentBillController {

    @Autowired
    IOrderPaymentBillService billService;

    @ApiOperation(value = "应付出账单列表(主订单/子订单)")
    @PostMapping("/findPaymentBillByPage")
    public CommonResult<CommonPageResult<OrderPaymentBillVO>> findPaymentBillByPage(@RequestBody @Valid QueryPaymentBillForm form) {
        IPage<OrderPaymentBillVO> pageList = billService.findPaymentBillByPage(form);
        CommonPageResult<OrderPaymentBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "账单数列表(主订单/子订单)")
    @PostMapping("/findPaymentBillNum")
    public CommonResult<Map<String,Object>> findPaymentBillNum(@RequestBody @Valid QueryPaymentBillNumForm form) {
        Map<String,Object> result = billService.findPaymentBillNum(form);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "未出账订单数列表(主订单/子订单)")
    @PostMapping("/findNotPaidBillByPage")
    public CommonResult<CommonPageResult<PaymentNotPaidBillVO>> findNotPaidBillByPage(@RequestBody @Valid QueryNotPaidBillForm form) {
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
        return CommonResult.error(ResultEnum.OPR_FAIL);
    }

    @ApiOperation(value = "生成账单编号,应收应付公用")
    @PostMapping("/createBillNo")
    public CommonResult<String> createBillNo() {
        String billNo = StringUtils.loadNum(CommonConstant.B,12);
        while (true){
            if(!billService.isExistBillNo(billNo)){//重复
                billNo = StringUtils.loadNum(CommonConstant.B,12);
            }else {
                break;
            }
        }
        return CommonResult.success(billNo);
    }


    @ApiOperation(value = "预览应付账单")
    @PostMapping("/viewPaymentBill")
    public CommonResult<Map<String,Object>> viewPaymentBill(@RequestBody @Valid ViewFBillForm form) {
        List<OrderPaymentBillDetailForm> billDetailForms = form.getBillDetailForms();
        List<Long> costIds = new ArrayList<>();
        for (OrderPaymentBillDetailForm billDetailForm : billDetailForms) {
            costIds.add(billDetailForm.getCostId());
        }
        Map<String,Object> resultMap = new HashMap<>();
        List<ViewBilToOrderVO> list = billService.viewPaymentBill(costIds);
        resultMap.put(CommonConstant.LIST,list);//分页数据
        List<SheetHeadVO> sheetHeadVOS = billService.findSheetHead(costIds);
        resultMap.put(CommonConstant.SHEET_HEAD,sheetHeadVOS);//表头
        return CommonResult.success(resultMap);
    }


}
