package com.jayud.finance.service;

import com.jayud.finance.vo.FinanceAccountVO;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;

import java.util.List;

/**
 * 数据处理
 */
public interface DataProcessingService {


    /**
     * 应收/应付对账单列表,应收/应付对账单审核列表,应收/应付财务应付对账单列表
     *
     * @param list
     * @param type 0:应收,1:应付
     */
    public void processingPaymentBillDetail(List<OrderPaymentBillDetailVO> list, Integer type);

    /**
     * 财务核算数据处理
     * @param type 0:应收,1:应付
     */
    public void processingFinanceAccount(List<FinanceAccountVO> list, Integer type);
}
