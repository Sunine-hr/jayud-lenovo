package com.jayud.oms.service.impl;

import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.oms.service.ICostCommonService;
import com.jayud.oms.service.IOrderPaymentCostService;
import com.jayud.oms.service.IOrderReceivableCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class CostCommonServiceImpl implements ICostCommonService {

    @Autowired
    private IOrderReceivableCostService orderReceivableCostService;
    @Autowired
    private IOrderPaymentCostService orderPaymentCostService;

    /**
     * 统计应收/应付待处理费用审核
     */
    @Override
    public Integer auditPendingExpenses(String subType, List<Long> legalIds) {
        Set<String> orderNos = new HashSet<>();
        String key= SubOrderSignEnum.MAIN.getSignOne().equals(subType)?"mainOrderNo":"orderNo";
        //查询应付待费用审核
        List<Map<String, Object>> paymentCostMap = this.orderPaymentCostService.getPendingExpenseApproval(subType, null, legalIds);
        //查询应收待费用审核
        List<Map<String, Object>> receivableCostMap = this.orderReceivableCostService.getPendingExpenseApproval(subType, null, legalIds);
        paymentCostMap.stream().filter(e -> e.get(key) != null).forEach(e -> orderNos.add(e.get(key).toString()));
        receivableCostMap.stream().filter(e -> e.get(key) != null).forEach(e -> orderNos.add(e.get(key).toString()));
        return orderNos.size();
    }
}
