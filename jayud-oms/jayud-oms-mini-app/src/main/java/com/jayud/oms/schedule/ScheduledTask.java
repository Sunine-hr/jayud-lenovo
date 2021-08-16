package com.jayud.oms.schedule;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.model.bo.GetOrderDetailForm;
import com.jayud.oms.model.enums.EmploymentFeeStatusEnum;
import com.jayud.oms.model.po.DriverOrderInfo;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.IDriverOrderInfoService;
import com.jayud.oms.service.IMsgPushRecordService;
import com.jayud.oms.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时任务
 */
@Component
@Slf4j
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private IDriverOrderInfoService driverOrderInfoService;
    @Autowired
    private TmsClient tmsClient;

    /**
     * 自动司机接单
     * corn表达式格式：秒 分 时 日 月 星期 年（可选）
     * 0/7 * * * * ?        代表每7秒执行一次
     * 0 0 4 1 * ?          每月1号凌晨4点触发
     */
    @Scheduled(cron = "0/59 * * * * ?")
    public void autoDriverReceivingOrder() {
        log.info("*********   定时自动司机接单 :" + DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN) + "  **************");
        StopWatch stopWatch = new StopWatch();
        // 开始时间
        stopWatch.start();
        List<DriverOrderInfo> list = driverOrderInfoService.list();
        List<String> orderNos = list.stream().map(DriverOrderInfo::getOrderNo).collect(Collectors.toList());

        Object data = this.tmsClient.getDriverPendingOrder(orderNos).getData();

        JSONArray jsonArray = new JSONArray(data);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String orderNo = jsonObject.getStr("orderNo");
            Long driverInfoId = jsonObject.getLong("driverInfoId");
            Long jockeyId = jsonObject.getLong("jockeyId");
            DriverOrderInfo driverOrderInfo = new DriverOrderInfo();
            driverOrderInfo.setDriverId(driverInfoId);
            driverOrderInfo.setJockeyId(jockeyId);
            driverOrderInfo.setOrderNo(orderNo);
            driverOrderInfo.setTime(LocalDateTime.now());
            driverOrderInfo.setStatus(EmploymentFeeStatusEnum.SUBMIT.getCode());
            JSONObject tmp = new JSONObject(this.tmsClient.getTmsOrderByOrderNo(orderNo).getData());
            driverOrderInfo.setOrderId(tmp.getLong("id"));
            this.driverOrderInfoService.save(driverOrderInfo);
        }

        // 结束时间
        stopWatch.stop();
        log.info("********* 定时自动司机接单 (单位:秒): " + stopWatch.getTotalTimeSeconds() + " 秒. **************");
    }


}
