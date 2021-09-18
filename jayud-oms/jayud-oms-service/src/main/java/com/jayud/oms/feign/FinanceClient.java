package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.oms.model.po.CustomsFinanceFeeRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * customs模块消费finance模块的接口
 */
@FeignClient(value = "jayud-finance-web")
public interface FinanceClient {

    /**
     * 获取云报关-金蝶财务费用项对应关系
     */
    @RequestMapping(value = "/api/getCustomsFinanceFeeRelation")
    ApiResult<Map<String, CustomsFinanceFeeRelation>> getCustomsFinanceFeeRelation();


    /**
     * 根据原始币种和兑换币种获取汇率
     *
     * @return
     */
    @RequestMapping(value = "/api/getExchangeRates")
    public ApiResult<Map<String, BigDecimal>> getExchangeRates(@RequestParam("dcCode") String dcCode,
                                                               @RequestParam("month") String month);

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
                                                               @RequestParam("subType") String subType);

    /**
     * 应收/应付账单状态待处理数量
     *
     * @param userName
     * @param type     类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/getPendingBillStatusNum")
    public ApiResult<Map<String, Integer>> getPendingBillStatusNum(@RequestParam("userName") String userName,
                                                                   @RequestParam("legalIds") List<Long> legalIds,
                                                                   @RequestParam("type") Integer type,
                                                                   @RequestParam("isMain") boolean isMain,
                                                                   @RequestParam("subType") String subType);
}
