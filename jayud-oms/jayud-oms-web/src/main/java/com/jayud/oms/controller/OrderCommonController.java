package com.jayud.oms.controller;


import com.jayud.common.CommonResult;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.vo.InputCostVO;
import com.jayud.oms.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/orderCommon")
@Api(tags = "订单通用接口")
public class OrderCommonController {

    @Autowired
    private IOrderInfoService orderInfoService;

    @ApiOperation(value = "录入费用")
    @PostMapping(value = "/saveOrUpdateCost")
    public CommonResult saveOrUpdateCost(@RequestBody InputCostForm form) {
        if (form == null || form.getMainOrderId() == null || form.getPaymentCostList() == null ||
            form.getReceivableCostList() == null || form.getReceivableCostList().size() == 0 ||
            form.getPaymentCostList().size() == 0) {
            return CommonResult.error(400,"参数不合法");
        }
        if("submit_main".equals(form.getCmd()) || "submit_sub".equals(form.getCmd())){
            List<InputPaymentCostForm> paymentCostForms = form.getPaymentCostList();
            List<InputReceivableCostForm> receivableCostForms = form.getReceivableCostList();
            for (InputPaymentCostForm paymentCost : paymentCostForms) {
                if(paymentCost.getCustomerName() == null || "".equals(paymentCost.getCustomerName())
                || paymentCost.getCostCode() == null || "".equals(paymentCost.getCostCode())
                || paymentCost.getUnitPrice() == null || paymentCost.getNumber() == null
                || paymentCost.getCurrencyCode() == null || "".equals(paymentCost.getCurrencyCode())
                || paymentCost.getAmount() == null || paymentCost.getExchangeRate() == null
                || paymentCost.getChangeAmount() == null){
                    return CommonResult.error(400,"参数不合法");
                }
            }
            for (InputReceivableCostForm receivableCost : receivableCostForms) {
                if(receivableCost.getCustomerName() == null || "".equals(receivableCost.getCustomerName())
                        || receivableCost.getCustomerCode() == null || "".equals(receivableCost.getCustomerCode())
                        || receivableCost.getCostCode() == null || "".equals(receivableCost.getCostCode())
                        || receivableCost.getUnitPrice() == null || receivableCost.getNumber() == null
                        || receivableCost.getCurrencyCode() == null || "".equals(receivableCost.getCurrencyCode())
                        || receivableCost.getAmount() == null || receivableCost.getExchangeRate() == null
                        || receivableCost.getChangeAmount() == null){
                    return CommonResult.error(400,"参数不合法");
                }
            }
        }
        boolean result = orderInfoService.saveOrUpdateCost(form);
        if(!result){
            return CommonResult.error(400,"调用失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "费用详情,id= 主订单ID")
    @PostMapping(value = "/getCostDetail")
    public CommonResult getCostDetail(@RequestBody GetCostDetailForm form) {
        InputCostVO inputCostVO = orderInfoService.getCostDetail(form);
        return CommonResult.success(inputCostVO);
    }



    @ApiOperation(value = "费用审核")
    @PostMapping(value = "/auditCost")
    public CommonResult auditCost(@RequestBody AuditCostForm form) {
        boolean result = orderInfoService.auditCost(form);
        if(!result){
            return CommonResult.error(400,"调用失败");
        }
        return CommonResult.success();
    }




}

