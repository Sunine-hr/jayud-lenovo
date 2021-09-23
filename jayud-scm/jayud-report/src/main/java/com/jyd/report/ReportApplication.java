package com.jyd.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

/**
 * 积木报表服务启动类
 */
@SpringBootApplication
@RefreshScope
@ComponentScan(basePackages = {"com.jyd","org.jeecg.modules.jmreport"})
public class ReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class,args);
    }
}
