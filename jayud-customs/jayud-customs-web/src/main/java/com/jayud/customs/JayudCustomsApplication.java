package com.jayud.customs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * jayud-tms模块启动类
 */
@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableDiscoveryClient
@EnableTransactionManagement
public class JayudCustomsApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudCustomsApplication.class,args); }
}
