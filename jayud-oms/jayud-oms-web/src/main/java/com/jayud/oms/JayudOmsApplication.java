package com.jayud.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author larry
 * @Date 2020/4/13
 * @Time 14:10
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.jayud.common","com.jayud.oms"})
public class JayudOmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudOmsApplication.class,args); }
}
