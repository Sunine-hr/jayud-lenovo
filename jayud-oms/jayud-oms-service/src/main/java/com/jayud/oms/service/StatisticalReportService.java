package com.jayud.oms.service;

import com.jayud.oms.model.bo.QueryStatisticalReport;

import java.util.List;
import java.util.Map;

public interface StatisticalReportService {
    // 获取客服待处理统计
    List<Map<String, Object>> getCSPendingNum();

    /**
     * 订单汇总
     * @param form
     * @return
     */
    List<Map<String, Integer>> getMainOrderSummary(QueryStatisticalReport form);

    /**
     * 订单排行榜
     * @param form
     * @return
     */
    List<Map<String, Object>> getOrderRanking(QueryStatisticalReport form);

    /**
     * 营业额统计
     * @param form
     * @return
     */
    Map<String, Object> getTurnoverStatistics(QueryStatisticalReport form);

    /**
     * 汇款情况
     * @param form
     * @return
     */
    Map<String, Object> remittanceStatus(QueryStatisticalReport form);

    /**
     * 客户未回款
     * @param form
     * @return
     */
    List<Map<String, Object>> customerUncollectedPay(QueryStatisticalReport form);
}
