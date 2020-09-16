package com.jayud.customs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * jayud-tms模块启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.jayud.commom","com.jayud.customs"})
public class JayudCustomsApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudCustomsApplication.class,args); }
}
