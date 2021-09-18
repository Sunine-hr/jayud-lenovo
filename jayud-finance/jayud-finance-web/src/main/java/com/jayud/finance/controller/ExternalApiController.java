package com.jayud.finance.controller;

import cn.hutool.core.map.MapUtil;
import com.jayud.common.ApiResult;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.po.CustomsFinanceFeeRelation;
import com.jayud.finance.service.ICurrencyRateService;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.service.PreloadService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(tags = "finance对外接口")
@Slf4j
public class ExternalApiController {
    @Autowired
    PreloadService preloadService;
    @Autowired
    private ICurrencyRateService currencyRateService;
    @Autowired
    private IOrderReceivableBillDetailService receivableBillDetailService;
    @Autowired
    private IOrderPaymentBillDetailService paymentBillDetailService;
    @Autowired
    private OmsClient omsClient;

    /**
     * 获取云报关-金蝶财务费用项对应关系
     *
     * @return
     */
    @RequestMapping(value = "/api/getCustomsFinanceFeeRelation")
    public ApiResult getCustomsFinanceFeeRelation() {
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = preloadService.getFeeRelationMap();
        return ApiResult.ok(feeRelationMap);
    }

    /**
     * 根据原始币种和兑换币种获取汇率
     *
     * @return
     */
    @RequestMapping(value = "/api/getExchangeRates")
    public ApiResult<Map<String, BigDecimal>> getExchangeRates(@RequestParam("dcCode") String dcCode,
                                                               @RequestParam("month") String month) {
        month = month.length() > 7 ? month.substring(0, 7) : month;//把时间处理到月
        return ApiResult.ok(this.currencyRateService.getExchangeRates(dcCode, month));
    }


    /**
     * 应收/应付账单状态统计
     *
     * @param userName
     * @param type     类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/getBillingStatusNum")
    public ApiResult<Map<String, Integer>> getBillingStatusNum(@RequestParam("userName") String userName,
                                                               @RequestParam("type") Integer type,
                                                               @RequestParam("isMain") boolean isMain,
                                                               @RequestParam("subType") String subType) {
        List<Map<String, Object>> list = new ArrayList<>();
        switch (type) {
            case 0:
                list = this.receivableBillDetailService.getBillingStatusNum(userName, isMain, subType);
                break;
            case 1:
                list = this.paymentBillDetailService.getBillingStatusNum(userName, isMain, subType);
                break;
        }
        Map<String, Integer> result = list.stream().collect(Collectors.toMap(e -> e.get("auditStatus").toString(), e -> MapUtil.getInt(e, "num")));
        return ApiResult.ok(result);
    }


    /**
     * 应收/应付账单状态待处理数量
     *
     * @param userName
     * @param type     类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/getPendingBillStatusNum")
    public ApiResult<Map<String, Integer>> getPendingBillStatusNum(@RequestParam(value = "userName", required = false) String userName,
                                                                   @RequestParam(value = "legalIds", required = false) List<Long> legalIds,
                                                                   @RequestParam("type") Integer type,
                                                                   @RequestParam("isMain") boolean isMain,
                                                                   @RequestParam("subType") String subType) {
        List<Map<String, Object>> list = new ArrayList<>();
        switch (type) {
            case 0:
                List<Long> costIds = this.omsClient.getReBillCostIdsBySubType(legalIds, userName, subType).getData();
                if (CollectionUtils.isEmpty(costIds)) {
                    return ApiResult.ok(new HashMap<String, Integer>(0));
                }
                list = this.receivableBillDetailService.getPendingBillStatusNum(costIds, userName, legalIds, isMain, subType);
                break;
            case 1:
                costIds = this.omsClient.getPayBillCostIdsBySubType(legalIds, userName, subType).getData();
                if (CollectionUtils.isEmpty(costIds)) {
                    return ApiResult.ok(new HashMap<String, Integer>(0));
                }
                list = this.paymentBillDetailService.getPendingBillStatusNum(costIds, userName, legalIds, isMain, subType);
                break;
        }
        Map<String, Integer> result = list.stream().collect(Collectors.toMap(e -> e.get("auditStatus").toString(), e -> MapUtil.getInt(e, "num")));
        return ApiResult.ok(result);
    }


}
