package com.jayud.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * jayud-tms模块启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class JayudTmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudTmsApplication.class,args); }
}
