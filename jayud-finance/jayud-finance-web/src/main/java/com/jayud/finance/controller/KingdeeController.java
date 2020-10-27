package com.jayud.finance.controller;

import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.finance.bo.CustomsFinanceCoRelationForm;
import com.jayud.finance.bo.CustomsFinanceFeeRelationForm;
import com.jayud.finance.bo.PayableHeaderForm;
import com.jayud.finance.bo.ReceivableHeaderForm;
import com.jayud.finance.enums.FormIDEnum;
import com.jayud.finance.po.CustomsFinanceCoRelation;
import com.jayud.finance.po.CustomsFinanceFeeRelation;
import com.jayud.finance.po.Payable;
import com.jayud.finance.po.Receivable;
import com.jayud.finance.service.KingdeeService;
import com.jayud.finance.service.PreloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author william
 * @description
 * @Date: 2020-09-08 14:55
 */
@RestController
@RequestMapping("/finance/kingdee")
@Api(tags = "财务管理")
public class KingdeeController {
    @Autowired
    KingdeeService service;
    @Autowired
    PreloadService preloadService;

    /**
     * 推送应收单到金蝶
     * by william
     *
     * @param reqForm
     * @return
     */
    @PostMapping("/receivable/push")
    @ApiOperation(value = "推送应收单")
    public CommonResult saveReceivableBill(@RequestBody ReceivableHeaderForm reqForm) {
        return service.saveReceivableBill(FormIDEnum.RECEIVABLE.getFormid(), reqForm);
    }

    /**
     * 推送应付单到金蝶
     * by william
     *
     * @param reqForm
     * @return
     */
    @PostMapping("/payable/push")
    @ApiOperation(value = "推送应付单")
    public CommonResult savePayableBill(@RequestBody PayableHeaderForm reqForm) {
        return service.savePayableBill(FormIDEnum.PAYABLE.getFormid(), reqForm);
    }

    /**
     * 根据传入的单号查询金蝶的应付账单数据
     *
     * @param param
     * @return
     */
    @PostMapping("/payable/pull")
    @ApiOperation(value = "查询应付单")
    public CommonResult getPayableBill(@RequestBody Map<String, Object> param) {
        String orderNo = MapUtil.getStr(param, "orderNo");
        if (StringUtils.isBlank(orderNo)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "单号为必填");
        }
        return service.getInvoice(orderNo, Payable.class);
    }

    /**
     * 根据传入的单号查询金蝶的应收账单数据
     *
     * @param param
     * @return
     */
    @PostMapping("/receivable/pull")
    @ApiOperation(value = "查询应收单")
    public CommonResult getReceivableBill(@RequestBody Map<String, Object> param) {
        String orderNo = MapUtil.getStr(param, "orderNo");
        if (StringUtils.isBlank(orderNo)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "单号为必填");
        }
        return service.getInvoice(orderNo, Receivable.class);
    }

    @PostMapping("/relation/add/fee")
    @ApiOperation(value = "添加 云报关-金蝶 费用关系")
    public CommonResult addFee(@RequestBody @Valid CustomsFinanceFeeRelationForm form) {
        CustomsFinanceFeeRelation relation = ConvertUtil.convert(form, CustomsFinanceFeeRelation.class);
        Map<String, CustomsFinanceFeeRelation> stringCustomsFinanceFeeRelationMap = preloadService.addFeeRelation(relation);
        return CommonResult.success(stringCustomsFinanceFeeRelationMap);
    }

    @PostMapping("/relation/add/company")
    @ApiOperation(value = "添加 云报关-金蝶 客户关系")
    public CommonResult addFee(@RequestBody @Valid CustomsFinanceCoRelationForm form) {
        CustomsFinanceCoRelation relation = ConvertUtil.convert(form, CustomsFinanceCoRelation.class);
        Map<String, CustomsFinanceCoRelation> stringCustomsFinanceCoRelationMap = preloadService.addCompanyRelation(relation);
        return CommonResult.success(stringCustomsFinanceCoRelationMap);
    }
}
