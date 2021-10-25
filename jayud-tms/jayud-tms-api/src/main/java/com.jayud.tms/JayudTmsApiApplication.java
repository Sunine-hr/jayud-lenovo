package com.jayud.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * jayud-tms-api模块启动类
 */
@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableDiscoveryClient
@EnableFeignClients
public class JayudTmsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudTmsApiApplication.class,args); }
}
