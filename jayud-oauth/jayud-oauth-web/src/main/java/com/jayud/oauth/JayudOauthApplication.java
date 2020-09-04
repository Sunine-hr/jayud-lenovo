package com.jayud.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * jayud-oauth模块启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.jayud.common","com.jayud.oauth"})
@RefreshScope
public class JayudOauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudOauthApplication.class,args); }
}
