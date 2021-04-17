package com.jayud.mall.schedule;

import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.service.IAccountPayableService;
import com.jayud.mall.service.IAccountReceivableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时任务
 */
@Component
@EnableAsync
@Slf4j
public class ScheduledTask {

    @Autowired
    IAccountReceivableService accountReceivableService;
    @Autowired
    IAccountPayableService accountPayableService;

    /**
     * corn表达式格式：秒 分 时 日 月 星期 年（可选）
     * 0/7 * * * * ?        代表每7秒执行一次
     * 0 0 4 1 * ?          每月1号凌晨4点触发
     */

    @Scheduled(cron = "0 0 4 1 * ?")
    @Async
    public void scheduledTaskV1() {
        MonthlyStatementForm form = new MonthlyStatementForm();
        LocalDateTime startTime =  LocalDateTime.now();
        LocalDateTime newTime = startTime.minusDays(1);//当前日期，减去一天
        form.setMonthlyStatement(newTime);
        accountReceivableService.createRecMonthlyStatement(form);
        log.info("定时任务1:生成应收月结账单(创建应收对账单),执行结束");
    }

    @Scheduled(cron = "0 0 4 1 * ?")
    @Async
    public void scheduledTaskV2() {
        MonthlyStatementForm form = new MonthlyStatementForm();
        LocalDateTime startTime =  LocalDateTime.now();
        LocalDateTime newTime = startTime.minusDays(1);//当前日期，减去一天
        form.setMonthlyStatement(newTime);
        accountPayableService.createPayMonthlyStatement(form);
        log.info("定时任务2:生成应付月结账单(创建应付对账单),执行结束");
    }

    private void scheduledTask() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
