package com.jayud.oms.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.oms.model.bo.GetOrderDetailForm;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务
 */
@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    private IOrderInfoService orderInfoService;

    /**
     * 同步主订单数据
     * corn表达式格式：秒 分 时 日 月 星期 年（可选）
     * 0/7 * * * * ?        代表每7秒执行一次
     * 0 0 4 1 * ?          每月1号凌晨4点触发
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void syncMainOrderData() {
        log.info("*********   定时同步主订单数据任务执行   **************");
        QueryWrapper<OrderInfo> condition = new QueryWrapper<>();
        condition.lambda().select(OrderInfo::getId, OrderInfo::getClassCode).eq(OrderInfo::getIsComplete, true);
        List<OrderInfo> orderInfos = this.orderInfoService.getBaseMapper().selectList(condition);

        for (OrderInfo orderInfo : orderInfos) {
            GetOrderDetailForm form = new GetOrderDetailForm();
            form.setMainOrderId(orderInfo.getId());
            form.setClassCode(orderInfo.getClassCode());
            InputOrderVO orderDetail = this.orderInfoService.getOrderDetail(form);
            boolean isComplete = true;
            if (OrderStatusEnum.CBG.getCode().equals(form.getClassCode())) {
                //纯报关所有通关确认
                List<InputSubOrderCustomsVO> subOrders = orderDetail.getOrderCustomsForm().getSubOrders();
                long count = subOrders.stream().filter(e -> OrderStatusEnum.CUSTOMS_C_6.getDesc().equals(e.getStatusDesc())).count();
                if (count != subOrders.size()) {
                    isComplete = false;
                }
            }
            long count = 0;
            //报关是放行审核
            InputOrderCustomsVO orderCustomsForm = orderDetail.getOrderCustomsForm();
            if (orderCustomsForm != null) {
                List<InputSubOrderCustomsVO> subOrders = orderDetail.getOrderCustomsForm().getSubOrders();
                count = subOrders.stream().filter(e -> OrderStatusEnum.CUSTOMS_C_5.getDesc().equals(e.getStatusDesc())).count();
                if (subOrders.size() != count) isComplete = false;
            }
            //中港是确认签收
            InputOrderTransportVO orderTransportForm = orderDetail.getOrderTransportForm();
            if (orderTransportForm != null) {
                if (!OrderStatusEnum.TMS_T_15.getCode().equals(orderTransportForm.getStatus())) isComplete = false;
            }
            //空运是确认签收
            InputAirOrderVO airOrderForm = orderDetail.getAirOrderForm();
            if (airOrderForm != null) {
                if (airOrderForm.getProcessStatus() != 1) isComplete = false;
            }
            //海运单订单
            InputSeaOrderVO seaOrderForm = orderDetail.getSeaOrderForm();
            if (seaOrderForm != null) {
                if (seaOrderForm.getProcessStatus() != 1) isComplete = false;
            }


        }


        log.info("*********   定时同步主订单数据任务结束   **************");
    }


}
