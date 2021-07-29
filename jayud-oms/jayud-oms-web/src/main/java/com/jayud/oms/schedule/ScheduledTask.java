package com.jayud.oms.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 */
@Component
@Slf4j
public class ScheduledTask {


    /**
     * corn表达式格式：秒 分 时 日 月 星期 年（可选）
     * 0/7 * * * * ?        代表每7秒执行一次
     * 0 0 4 1 * ?          每月1号凌晨4点触发
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void scheduledTaskV1() {
        log.info("*********   定时同步云报关报关单进程信息到OMS任务执行   **************");
        //获取订单
        log.info("*********   定时同步云报关报关单进程信息到OMS任务执行结束   **************");
    }


}
