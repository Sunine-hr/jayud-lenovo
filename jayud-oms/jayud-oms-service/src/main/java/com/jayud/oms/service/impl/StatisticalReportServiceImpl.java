package com.jayud.oms.service.impl;

import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.QueryStatisticalReport;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.po.OrderStatus;
import com.jayud.oms.service.ICostCommonService;
import com.jayud.oms.service.IOrderInfoService;
import com.jayud.oms.service.StatisticalReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collector;
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
                        num = (int) orderInfos.get().stream().filter(e -> e.getIsRejected() || OrderStatusEnum.MAIN_6.getCode().equals(e.getStatus().toString())).count();
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
}
