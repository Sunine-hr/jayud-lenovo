package com.jayud.oms.service.impl;

import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.oms.mapper.OrderInfoMapper;
import com.jayud.oms.model.po.OrderInfo;
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
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    /**
     * 统计应收/应付待处理费用审核
     */
    @Override
    public Integer auditPendingExpenses(String subType, List<Long> legalIds, List<String> orderNos) {
        Set<String> orderNosSet = new HashSet<>();
        String key = SubOrderSignEnum.MAIN.getSignOne().equals(subType) ? "mainOrderNo" : "orderNo";
        //查询应付待费用审核
        List<Map<String, Object>> paymentCostMap = this.orderPaymentCostService.getPendingExpenseApproval(subType, orderNos, legalIds);
        //查询应收待费用审核
        List<Map<String, Object>> receivableCostMap = this.orderReceivableCostService.getPendingExpenseApproval(subType, orderNos, legalIds);
        paymentCostMap.stream().filter(e -> e.get(key) != null).forEach(e -> orderNosSet.add(e.get(key).toString()));
        receivableCostMap.stream().filter(e -> e.get(key) != null).forEach(e -> orderNosSet.add(e.get(key).toString()));
        return orderNosSet.size();
    }

    /**
     * 查询所有未录用费用订单数量
     *
     * @param list
     * @param legalIds
     * @param subType
     * @return
     */
    @Override
    public Integer allUnemployedFeesNum(List<OrderInfo> list, List<Long> legalIds, String subType) {
        Integer num = this.orderInfoMapper.getAllCostNum(legalIds,subType);
        return list.size() - num;
    }
}
