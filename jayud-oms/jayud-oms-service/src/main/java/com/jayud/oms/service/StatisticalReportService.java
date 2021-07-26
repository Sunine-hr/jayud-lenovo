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
}
