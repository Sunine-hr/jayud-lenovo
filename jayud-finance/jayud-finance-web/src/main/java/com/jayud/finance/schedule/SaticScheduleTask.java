package com.jayud.finance.schedule;

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
    @Scheduled(cron = "0 0/1 * * * ?")
    private void statisticalProfitReport() {
        System.out.println("执行静态定时任务时间: " + LocalDateTime.now());
        //获取统计数据
        List<ProfitStatement> list = this.profitStatementService.statisticalProfitReport();
        //同步数据
        this.profitStatementService.synchronizeData(list);

    }
}
