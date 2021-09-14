package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.FinanceClient;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.QueryStatisticalReport;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.OrderInfoVO;
import com.jayud.oms.model.vo.StatisticsOrderBaseCostVO;
import com.jayud.oms.model.vo.StatisticsOrderBillDetailsVO;
import com.jayud.oms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    @Autowired
    private FinanceClient financeClient;

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);


    @Override
    public List<Map<String, Object>> getCSPendingNum() {
        Map<String, String> tmp = new LinkedHashMap<>();
        tmp.put("外部报关放行", "outPortPass");
        tmp.put("通关前审核", "portPassCheck");
        tmp.put("待录入费用", "pendingFees");
        tmp.put("费用审核", "feeCheck");
        tmp.put("待处理", "pending");
        tmp.put("应收账单提交财务", "reBillSubmitFinance");
        tmp.put("应付账单提交财务", "payBillSubmitFinance");
        tmp.put("开票申请", "invoicingRequisition");
        tmp.put("付款申请", "paymentApplication");


        List<Map<String, Object>> result = new ArrayList<>(tmp.size());

        ApiResult<List<Long>> legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = legalEntityByLegalName.getData();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<List<OrderInfo>> orderInfos = new AtomicReference<>();
        Map<String, Integer> unemployedFeesMap = new HashMap<>();
        String userName = UserOperator.getToken();
        fixedThreadPool.execute(() -> {
            try {
//                List<OrderInfo> list = this.orderInfoService.getByLegalEntityIds(legalIds);
                List<OrderInfo> list = this.orderInfoService.getBaseMapper().selectList(new QueryWrapper<>(new OrderInfo().setCreatedUser(userName)));
                orderInfos.set(list);
                //统计未录用订单数量
                Integer num = this.costCommonService.allUnemployedFeesNum(list, null, SubOrderSignEnum.MAIN.getSignOne(), userName);
                unemployedFeesMap.put("pendingFees", num);
                countDownLatch.countDown();
            } catch (Exception e) {
                log.warn("获取未录用费用数量线程报错", e);
                countDownLatch.countDown();
            }
        });

//        new Thread(() -> {
//            try {
//                List<OrderInfo> list = this.orderInfoService.getByLegalEntityIds(legalIds);
//                orderInfos.set(list);
//                //统计未录用订单数量
//                Integer num = this.costCommonService.allUnemployedFeesNum(list, legalIds, SubOrderSignEnum.MAIN.getSignOne());
//                unemployedFeesMap.put("pendingFees", num);
//                countDownLatch.countDown();
//            } catch (Exception e) {
//                log.warn("获取未录用费用数量线程报错", e);
//                countDownLatch.countDown();
//            }
//        }).start();

        Map<String, Integer> receivableStatusNum = this.financeClient.getBillingStatusNum(userName, 0, true, SubOrderSignEnum.MAIN.getSignOne()).getData();
        Map<String, Integer> payStatusNum = this.financeClient.getBillingStatusNum(userName, 1, true, SubOrderSignEnum.MAIN.getSignOne()).getData();
        tmp.forEach((k, v) -> {
            Map<String, Object> map = new HashMap<>();
            Integer num = 0;
            if (v != null) {
                switch (v) {
                    case "outPortPass":
//                        num = this.orderInfoService.pendingExternalCustomsDeclarationNum(legalIds);
                        num = this.orderInfoService.pendingExternalCustomsDeclarationNum(null, userName);
                        break;
                    case "portPassCheck":
//                        num = this.orderInfoService.filterGoCustomsAudit(null, legalIds).size();
                        num = this.orderInfoService.filterGoCustomsAudit(null, null, UserOperator.getToken()).size();
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
                        num = this.costCommonService.auditPendingExpenses(SubOrderSignEnum.MAIN.getSignOne(), legalIds, null,userName);
                        break;
                    case "pending":
                        if (orderInfos.get() != null) {
                            num = (int) orderInfos.get().stream().filter(e -> (e.getIsRejected() != null && e.getIsRejected())
                                    || OrderStatusEnum.MAIN_6.getCode().equals(e.getStatus().toString())).count();
                        }
                        break;
                    case "reBillSubmitFinance":
                        num = receivableStatusNum.getOrDefault("B_2", 0);
                        break;
                    case "payBillSubmitFinance":
                        num = payStatusNum.getOrDefault("B_2", 0);
                        break;
                    case "invoicingRequisition":
                        num = receivableStatusNum.getOrDefault("B_4", 0);
                        break;
                    case "paymentApplication":
                        num = payStatusNum.getOrDefault("B_4", 0);
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
        List<OrderInfoVO> orderInfos = this.orderInfoService.getBasicStatistics(form, legalIds, new OrderInfo());

        Map<String, List<OrderInfoVO>> group = orderInfos.stream().filter(e -> !StringUtils.isEmpty(e.getCreatedUser())).collect(Collectors.groupingBy(OrderInfoVO::getCreatedUser));

        List<String> orderNos = orderInfos.stream().map(OrderInfoVO::getOrderNo).collect(Collectors.toList());

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
            BigDecimal totalAmount = new BigDecimal(0);
            for (OrderInfoVO orderInfo : v) {
                Object amount = orderAmounts.getOrDefault(orderInfo.getOrderNo(), new BigDecimal(0));
                totalAmount = BigDecimalUtil.add(totalAmount, (BigDecimal) amount);
            }
            map.put("name", k);
            map.put("orderNum", v.size());
            map.put("amount", totalAmount);
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

        Map<String, BigDecimal> reCostMap = this.receivableCostService.getBaseStatisticsAllCost(form, legalIds, status).stream()
                .collect(Collectors.groupingBy(StatisticsOrderBaseCostVO::getOrderCreatedTime, Collectors.mapping(StatisticsOrderBaseCostVO::getChangeAmount,
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

//                .filter(e -> e.getCreatedTime() != null).collect().collect(Collectors.toMap(e -> DateUtils.LocalDateTime2Str(e.getCreatedTime()), e -> e.getChangeAmount()));

//        Map<String, BigDecimal> reCostMap = this.receivableCostService.statisticsMainOrderCost(form, legalIds, status)
//                .stream().filter(e -> e.get("createTime") != null).collect(Collectors.toMap(e -> e.get("createTime").toString(), e -> (BigDecimal) e.get("changeAmount")));
//        Map<String, BigDecimal> payCostMap = this.paymentCostService.statisticsMainOrderCost(form, legalIds, status)
//                .stream().filter(e -> e.get("createTime") != null).collect(Collectors.toMap(e -> e.get("createTime").toString(), e -> (BigDecimal) e.get("changeAmount")));
        Map<String, BigDecimal> payCostMap = this.paymentCostService.getBaseStatisticsAllCost(form, legalIds, status).stream()
                .collect(Collectors.groupingBy(StatisticsOrderBaseCostVO::getOrderCreatedTime, Collectors.mapping(StatisticsOrderBaseCostVO::getChangeAmount,
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

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

    /**
     * 回款情况
     *
     * @param form
     * @return
     */
    @Override
    public Map<String, Object> remittanceStatus(QueryStatisticalReport form) {
        List<String> status = new ArrayList<>();
        status.add(OrderStatusEnum.COST_0.getCode());
        status.add(OrderStatusEnum.COST_2.getCode());
        status.add(OrderStatusEnum.COST_3.getCode());
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

//        List<StatisticsOrderBaseCostVO> reCosts = this.receivableCostService.getBaseStatisticsAllCost(form, legalIds, status);

        Map<String, List<StatisticsOrderBillDetailsVO>> group = this.receivableCostService.statisticalMainOrderBillDetails(form, legalIds, status)
                .stream().collect(Collectors.groupingBy(StatisticsOrderBillDetailsVO::getCreateTime));

        List<BigDecimal> totalAmounts = new ArrayList<>();
        List<BigDecimal> amountReceiveds = new ArrayList<>();
        List<BigDecimal> uncollectedAmounts = new ArrayList<>();
        for (String suppleTimeDatum : form.getSuppleTimeData()) {
            List<StatisticsOrderBillDetailsVO> list = group.getOrDefault(suppleTimeDatum, new ArrayList<>());

            BigDecimal totalAmount = new BigDecimal(0);
            BigDecimal amountReceived = new BigDecimal(0);
            for (StatisticsOrderBillDetailsVO billCost : list) {
                BigDecimal heiXiaoAmount = BigDecimalUtil.add(billCost.getHeiXiaoLocalAmount(), billCost.getShortLocalAmount());
                totalAmount = BigDecimalUtil.add(billCost.getLocalMoney(), totalAmount);
                amountReceived = BigDecimalUtil.add(heiXiaoAmount, amountReceived);
            }
            totalAmounts.add(totalAmount);
            amountReceiveds.add(amountReceived);
            uncollectedAmounts.add(BigDecimalUtil.subtract(totalAmount, amountReceived));

        }
        Map<String, Object> map = new HashMap<>();
        map.put("date", form.getSuppleTimeData());
        map.put("totalAmounts", totalAmounts);
        map.put("amountReceiveds", amountReceiveds);
        map.put("uncollectedAmounts", uncollectedAmounts);
        map.put("timeUnit", form.getTimeUnit());
        return map;
    }

    /**
     * 客户未回款
     *
     * @param form
     * @return
     */
    @Override
    public List<Map<String, Object>> customerUncollectedPay(QueryStatisticalReport form) {
        List<String> status = new ArrayList<>();
        status.add(OrderStatusEnum.COST_0.getCode());
        status.add(OrderStatusEnum.COST_2.getCode());
        status.add(OrderStatusEnum.COST_3.getCode());
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

//        List<StatisticsOrderBaseCostVO> reCosts = this.receivableCostService.getBaseStatisticsAllCost(form, legalIds, status);

        Map<String, List<StatisticsOrderBillDetailsVO>> group = this.receivableCostService.statisticalMainOrderBillDetails(form, legalIds, status)
                .stream().collect(Collectors.groupingBy(e -> e.getLegalName() + "~" + e.getCustomerName()));

        List<Map<String, Object>> list = new ArrayList<>();

        group.forEach((k, v) -> {
            String customerName = k.split("~")[1];
            BigDecimal totalAmount = new BigDecimal(0);
            BigDecimal amountReceived = new BigDecimal(0);
            Map<String, Object> map = new HashMap<>();
            List<String> billNos = new ArrayList<>(v.size());
            for (StatisticsOrderBillDetailsVO billCost : v) {
                BigDecimal heiXiaoAmount = BigDecimalUtil.add(billCost.getHeiXiaoLocalAmount(), billCost.getShortLocalAmount());
                totalAmount = BigDecimalUtil.add(billCost.getLocalMoney(), totalAmount);
                amountReceived = BigDecimalUtil.add(heiXiaoAmount, amountReceived);
                billNos.add(billCost.getBillNo());
            }
            map.put("customerName", customerName);
            map.put("totalAmount", totalAmount);
            map.put("uncollectedAmount", BigDecimalUtil.subtract(totalAmount, amountReceived));
            map.put("billNos", billNos);
            list.add(map);
        });

        //根据金额排序
        list.sort(new Utilities.MapComparatorBigDesc("totalAmount"));
        return list;
    }

    /**
     * 主订单统计
     *
     * @param form
     * @return
     */
    @Override
    public Map<String, Object> statisticsMainOrder(QueryStatisticalReport form) {
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();
        List<OrderInfoVO> orderInfos = this.orderInfoService.getBasicStatistics(form, legalIds, new OrderInfo());

        Map<String, List<OrderInfoVO>> group = orderInfos.stream().filter(e -> e.getCreatedTimeStr() != null)
                .collect(Collectors.groupingBy(OrderInfoVO::getCreatedTimeStr));

        List<Integer> totalExecutingNums = new ArrayList<>();
        List<Integer> totalCompleteNums = new ArrayList<>();
        List<Integer> totalAbnormalNums = new ArrayList<>();
        for (String suppleTimeDatum : form.getSuppleTimeData()) {
            List<OrderInfoVO> list = group.getOrDefault(suppleTimeDatum, new ArrayList<>());

            Integer executingNum = 0;//执行中数量
            Integer completeNum = 0;//完成数量
            Integer abnormal = 0;//异常数量
            for (OrderInfoVO orderInfo : list) {
                if (OrderStatusEnum.MAIN_1.getCode().equals(orderInfo.getStatus().toString())
                        && (orderInfo.getIsRejected() == null || !orderInfo.getIsRejected())) {
                    ++executingNum;
                    continue;
                }
                if (OrderStatusEnum.MAIN_3.getCode().equals(orderInfo.getStatus().toString())
                        || (orderInfo.getIsRejected() != null && orderInfo.getIsRejected())) {
                    ++abnormal;
                    continue;
                }

            }
            totalExecutingNums.add(executingNum);
            totalCompleteNums.add(completeNum);
            totalAbnormalNums.add(abnormal);

        }
        Map<String, Object> map = new HashMap<>();
        map.put("date", form.getSuppleTimeData());
        map.put("totalExecutingNums", totalExecutingNums);
        map.put("totalCompleteNums", totalCompleteNums);
        map.put("totalAbnormalNums", totalAbnormalNums);
        map.put("timeUnit", form.getTimeUnit());
        return map;
    }


}
