package com.jayud.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * jayud-mall模块启动类(后端，管理员使用)
 * @author mfc
 * @description:
 * @date 2020/10/23 11:29
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.jayud.common","com.jayud.mall"})
public class JayudMallAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(JayudMallAdminApplication.class, args);
    }
}
