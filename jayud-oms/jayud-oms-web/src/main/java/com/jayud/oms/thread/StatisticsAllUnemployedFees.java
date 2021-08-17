//package com.jayud.oms.thread;
//
//import com.jayud.common.enums.SubOrderSignEnum;
//import com.jayud.oms.model.po.OrderInfo;
//import com.jayud.oms.service.ICostCommonService;
//import com.jayud.oms.service.IOrderInfoService;
//
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//
//public class StatisticsAllUnemployedFees implements Runnable {
//
//    private IOrderInfoService orderInfoService;
//    private ICostCommonService costCommonService;
//    private final List<Long> legalIds;
//    private final CountDownLatch countDownLatch;
//
//
//    public StatisticsAllUnemployedFees(IOrderInfoService orderInfoService, ICostCommonService costCommonService, List<Long> legalIds, CountDownLatch countDownLatch) {
//        this.orderInfoService = orderInfoService;
//        this.costCommonService = costCommonService;
//        this.legalIds = legalIds;
//        this.countDownLatch = countDownLatch;
//    }
//
//    @Override
//    public void run() {
//        try {
//            List<OrderInfo> list = this.orderInfoService.getByLegalEntityIds(legalIds);
//            orderInfos.set(list);
//            //统计未录用订单数量
//            Integer num = this.costCommonService.allUnemployedFeesNum(list, legalIds, SubOrderSignEnum.MAIN.getSignOne());
//            unemployedFeesMap.put("pendingFees", num);
//            countDownLatch.countDown();
//        } catch (Exception e) {
//            log.warn("获取未录用费用数量线程报错", e);
//            countDownLatch.countDown();
//        }
//    }
//}
