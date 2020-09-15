package com.jayud.customs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * jayud-tms模块启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.jayud.common", "com.jayud.customs"})
public class JayudCustomsApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudCustomsApplication.class,args); }
}
