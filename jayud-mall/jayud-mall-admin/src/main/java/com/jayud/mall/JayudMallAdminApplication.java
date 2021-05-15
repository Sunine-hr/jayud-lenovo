package com.jayud.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * jayud-mall模块启动类(后端，管理员使用)
 * @author mfc
 * @description:
 * @date 2020/10/23 11:29
 */
@SpringBootApplication(scanBasePackages = {"com.jayud.mall","com.jayud.common.exception"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling//项目启动类上增加注解@EnableScheduling，表示开启定时任务
public class JayudMallAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(JayudMallAdminApplication.class, args);
    }
}
