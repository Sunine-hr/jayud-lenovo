package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddAcctPayForm;
import com.jayud.scm.model.bo.AddExportTaxInvoiceForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AcctPay;
import com.jayud.scm.model.vo.AcctPayVO;
import com.jayud.scm.model.vo.ExportTaxInvoiceVO;
import com.jayud.scm.service.IAcctPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 付款单主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/acctPay")
@Api(tags = "付款单管理")
public class AcctPayController {

    @Autowired
    private IAcctPayService acctPayService;

    @ApiOperation(value = "根据id查询付款单信息")
    @PostMapping(value = "/getAcctPayById")
    public CommonResult<AcctPayVO> getAcctPayById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        AcctPayVO acctPayVO = acctPayService.getAcctPayById(id);
        return CommonResult.success(acctPayVO);
    }

    @ApiOperation(value = "新增或修改付款单信息")
    @PostMapping(value = "/saveOrUpdateAcctPay")
    public CommonResult saveOrUpdateAcctPay(@RequestBody AddAcctPayForm form) {
        boolean result = acctPayService.saveOrUpdateAcctPay(form);
        if(!result){
            return CommonResult.error(444,"添加或修改付款单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "付款")
    @PostMapping(value = "/payment")
    public CommonResult payment(@RequestBody AddAcctPayForm form) {

        AcctPay acctPay = acctPayService.getById(form.getId());
        if(acctPay.getIsPay() != null && acctPay.getIsPay().equals(1)){
            return CommonResult.error(444,"该订单已经确认付款，无法进行付款");
        }

        boolean result = acctPayService.payment(form);
        if(!result){
            return CommonResult.error(444,"付款失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "确认付款")
    @PostMapping(value = "/confirmPayment")
    public CommonResult confirmPayment(@RequestBody QueryCommonForm form) {
        boolean result = acctPayService.confirmPayment(form);
        if(!result){
            return CommonResult.error(444,"付款失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "取消付款")
    @PostMapping(value = "/cancelPayment")
    public CommonResult cancelPayment(@RequestBody QueryCommonForm form) {

        AcctPay acctPay = acctPayService.getById(form.getId());
        if(acctPay.getIsPay() != null && acctPay.getIsPay().equals(1)){
            return CommonResult.error(444,"该订单已经确认付款，无法进行取消付款");
        }

        boolean result = acctPayService.cancelPayment(form);
        if(!result){
            return CommonResult.error(444,"取消付款失败");
        }
        return CommonResult.success();
    }


}

