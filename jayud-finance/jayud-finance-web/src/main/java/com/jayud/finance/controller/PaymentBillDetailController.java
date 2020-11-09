package com.jayud.finance.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.bo.ApplyPaymentForm;
import com.jayud.finance.bo.QueryPaymentBillDetailForm;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;
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
@RequestMapping("/paymentBillDetail/")
@Api(tags = "应付对账")
public class PaymentBillDetailController {

    @Autowired
    IOrderPaymentBillDetailService billDetailService;

    @ApiOperation(value = "应付对账单列表")
    @PostMapping("/findPaymentBillDetailByPage")
    public CommonResult<CommonPageResult<OrderPaymentBillDetailVO>> findPaymentBillDetailByPage(@RequestBody @Valid QueryPaymentBillDetailForm form) {
        IPage<OrderPaymentBillDetailVO> pageList = billDetailService.findPaymentBillDetailByPage(form);
        CommonPageResult<OrderPaymentBillDetailVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "付款申请")
    @PostMapping("/applyPayment")
    public CommonResult applyPayment(@RequestBody @Valid ApplyPaymentForm form) {
        //1.财务对账审核通过
        OrderPaymentBillDetail orderPaymentBillDetail = billDetailService.getById(form.getBillDetailId());
        if(orderPaymentBillDetail != null && !BillEnum.B_4.getCode().equals(orderPaymentBillDetail.getAuditStatus())){
            return CommonResult.error(10000,"不满足付款申请的条件");
        }
        Boolean result = billDetailService.applyPayment(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "付款申请作废,billDetailId=列表里面取")
    @PostMapping("/applyPaymentCancel")
    public CommonResult applyPaymentCancel(@RequestBody Map<String,Object> param) {
        Long billDetailId = Long.parseLong(MapUtil.getStr(param, "billDetailId"));
        //1.财务开票申请审核不通过
        OrderPaymentBillDetail orderPaymentBillDetail = billDetailService.getById(billDetailId);
        if(orderPaymentBillDetail != null && !BillEnum.B_6_1.getCode().equals(orderPaymentBillDetail.getAuditStatus())){
            return CommonResult.error(10000,"不满足付款申请作废的条件");
        }
        Boolean result = billDetailService.applyPaymentCancel(billDetailId);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }


}
