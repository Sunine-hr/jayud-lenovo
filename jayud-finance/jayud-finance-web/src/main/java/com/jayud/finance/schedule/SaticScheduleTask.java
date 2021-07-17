package com.jayud.finance.schedule;

import cn.hutool.core.date.StopWatch;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.po.ProfitStatement;
import com.jayud.finance.service.IProfitStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Autowired
    private IProfitStatementService profitStatementService;

    /**
     * 统计利润报表
     */
    @Scheduled(cron = "0 0 0,13 * * ?")
    private void statisticalProfitReport() {
        System.out.println("开始同步利润报表数据: " + DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN));
        StopWatch stopWatch = new StopWatch();
        // 开始时间
        stopWatch.start();
        //获取统计数据
        List<ProfitStatement> list = this.profitStatementService.statisticalProfitReport();
        //同步数据
        this.profitStatementService.synchronizeData(list);
        // 结束时间
        stopWatch.stop();
        System.out.println("同步完成利润报表数据用时(单位:秒): " + stopWatch.getTotalTimeSeconds()+" 秒.");

    }
}
