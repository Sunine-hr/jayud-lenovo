package com.jayud.oms.service.impl;

import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.QueryStatisticalReport;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 统计图
 */
@Service
@Slf4j
public class StatisticalReportServiceImpl implements StatisticalReportService {
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private ICostCommonService costCommonService;
    @Autowired
    private IOrderReceivableCostService receivableCostService;
    @Autowired
    private IOrderPaymentCostService paymentCostService;

    @Override
    public List<Map<String, Object>> getCSPendingNum() {
        Map<String, String> tmp = new LinkedHashMap<>();
        tmp.put("外部报关放行", "outPortPass");
        tmp.put("通关前审核", "portPassCheck");
        tmp.put("待录入费用", "pendingFees");
        tmp.put("费用审核", "feeCheck");
        tmp.put("待处理", "pending");

        List<Map<String, Object>> result = new ArrayList<>(tmp.size());

        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<List<OrderInfo>> orderInfos = new AtomicReference<>();
        Map<String, Integer> unemployedFeesMap = new HashMap<>();


        new Thread(() -> {
            try {
                List<OrderInfo> list = this.orderInfoService.getByLegalEntityIds(legalIds);
                orderInfos.set(list);
                //统计未录用订单数量
                Integer num = this.costCommonService.allUnemployedFeesNum(list, legalIds, SubOrderSignEnum.MAIN.getSignOne());
                unemployedFeesMap.put("pendingFees", num);
                System.out.println(Thread.currentThread().getName() + ":使用lambda表达式创建线程");
                countDownLatch.countDown();
            } catch (Exception e) {
                log.warn("获取未录用费用数量线程报错", e);
                countDownLatch.countDown();
            }
        }).start();

        tmp.forEach((k, v) -> {
            Map<String, Object> map = new HashMap<>();
            Integer num = 0;
            if (v != null) {
                switch (v) {
                    case "outPortPass":
                        num = this.orderInfoService.pendingExternalCustomsDeclarationNum(legalIds);
                        break;
                    case "portPassCheck":
                        num = this.orderInfoService.filterGoCustomsAudit(null, legalIds).size();
                        break;
                    case "pendingFees":
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            throw new JayudBizException("待处理线程错误", e);
                        }
                        num = unemployedFeesMap.get(v);
                        break;
                    case "feeCheck":
                        num = this.costCommonService.auditPendingExpenses(SubOrderSignEnum.MAIN.getSignOne(), legalIds, null);
                        break;
                    case "pending":
                        num = (int) orderInfos.get().stream().filter(e -> (e.getIsRejected() != null && e.getIsRejected())
                                || OrderStatusEnum.MAIN_6.getCode().equals(e.getStatus().toString())).count();
                        break;
                }

            }
            map.put("menusName", k);
            map.put("num", num == null ? 0 : num);
            result.add(map);
        });
        return result;
    }

    /**
     * 订单汇总
     *
     * @param form
     * @return
     */
    @Override
    public List<Map<String, Integer>> getMainOrderSummary(QueryStatisticalReport form) {
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();
        List<Map<String, Integer>> results = this.orderInfoService.getMainOrderSummary(form, legalIds);
//        Map<String, Long> group = list.stream().collect(Collectors.groupingBy(OrderInfo::getLegalName, Collectors.counting()));
//        List<Map<String, Object>> results = new ArrayList<>();
//        group.forEach((k, v) -> {
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", k);
//            map.put("value", v);
//            results.add(map);
//        });
        return results;
    }

    /**
     * 订单排行榜
     *
     * @param form
     * @return
     */
    @Override
    public List<Map<String, Object>> getOrderRanking(QueryStatisticalReport form) {
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();
        List<OrderInfo> orderInfos = this.orderInfoService.getBasicStatistics(form, legalIds, new OrderInfo());

        Map<String, List<OrderInfo>> group = orderInfos.stream().filter(e -> !StringUtils.isEmpty(e.getCreatedUser())).collect(Collectors.groupingBy(OrderInfo::getCreatedUser));

        List<String> orderNos = orderInfos.stream().map(OrderInfo::getOrderNo).collect(Collectors.toList());

//        Map<String, Map<String, Integer>> mainOrder = new HashMap<>();
//        orderInfos.forEach(e -> {
//            Map<String, Integer> orderNum = mainOrder.get(e.getOrderNo());
//            if (orderNum == null) {
//                orderNum = new HashMap<>();
//                orderNum.put(e.getCreatedUser(), 1);
//                mainOrder.put(e.getOrderNo(), orderNum);
//            } else {
//                orderNum.put(e.getCreatedUser(), orderNum.getOrDefault(e.getCreatedUser(), 0) + 1);
//            }
//        });
        List<String> status = new ArrayList<>();
        status.add(OrderStatusEnum.COST_0.getCode());
        status.add(OrderStatusEnum.COST_2.getCode());
        status.add(OrderStatusEnum.COST_3.getCode());
        //统计应收金额
        Map<String, Object> orderAmounts = this.receivableCostService.statisticsOrderAmount(orderNos, SubOrderSignEnum.MAIN.getSignOne(), status)
                .stream().collect(Collectors.toMap(e -> e.get("mainOrderNo").toString(), e -> e.get("changeAmount")));
        List<Map<String, Object>> results = new ArrayList<>();
        group.forEach((k, v) -> {
            Map<String, Object> map = new HashMap<>();
            v.forEach(e -> {
                Object amount = orderAmounts.getOrDefault(e.getOrderNo(), new BigDecimal(0));
                map.put("amount", amount);
            });
            map.put("name", k);
            map.put("orderNum", v.size());
            results.add(map);
        });
        //根据金额排序
        results.sort(new Utilities.MapComparatorBigDesc("amount")
                .thenComparing(new Utilities.MapComparatorIntDesc("orderNum")));
        return results;
    }

    /**
     * 营业额统计
     *
     * @param form
     * @return
     */
    @Override
    public Map<String, Object> getTurnoverStatistics(QueryStatisticalReport form) {

        List<String> status = new ArrayList<>();
        status.add(OrderStatusEnum.COST_0.getCode());
        status.add(OrderStatusEnum.COST_2.getCode());
        status.add(OrderStatusEnum.COST_3.getCode());
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Map<String, BigDecimal> reCostMap = this.receivableCostService.statisticsMainOrderCost(form, legalIds, status)
                .stream().filter(e -> e.get("createTime") != null).collect(Collectors.toMap(e -> e.get("createTime").toString(), e -> (BigDecimal) e.get("changeAmount")));
        Map<String, BigDecimal> payCostMap = this.paymentCostService.statisticsMainOrderCost(form, legalIds, status)
                .stream().filter(e -> e.get("createTime") != null).collect(Collectors.toMap(e -> e.get("createTime").toString(), e -> (BigDecimal) e.get("changeAmount")));

        List<BigDecimal> reCosts = new ArrayList<>();
        List<BigDecimal> payCosts = new ArrayList<>();
        List<BigDecimal> profits = new ArrayList<>();
        for (String suppleTimeDatum : form.getSuppleTimeData()) {
            BigDecimal reCost = reCostMap.getOrDefault(suppleTimeDatum, new BigDecimal(0));
            BigDecimal payCost = payCostMap.getOrDefault(suppleTimeDatum, new BigDecimal(0));
            BigDecimal profit = BigDecimalUtil.subtract(reCost, payCost);
            reCosts.add(reCost);
            payCosts.add(payCost);
            profits.add(profit);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("date", form.getSuppleTimeData());
        map.put("reCosts", reCosts);
        map.put("payCosts", payCosts);
        map.put("profits", profits);
        map.put("timeUnit", form.getTimeUnit());
        return map;
    }


}
