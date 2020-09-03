package com.jayud.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * jayud-oauth模块启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class JayudOauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudOauthApplication.class,args); }
}
