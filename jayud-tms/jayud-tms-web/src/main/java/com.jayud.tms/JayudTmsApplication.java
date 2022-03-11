package com.jayud.tms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * jayud-tms模块启动类
 */
@SpringBootApplication(scanBasePackages = "com.jayud")
@MapperScan(basePackages = {"com.jayud.**.mapper"})
@EnableDiscoveryClient
@EnableFeignClients
public class JayudTmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudTmsApplication.class,args); }
}
