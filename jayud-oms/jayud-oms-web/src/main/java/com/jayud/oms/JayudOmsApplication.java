package com.jayud.oms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author larry
 * @Date 2020/4/13
 * @Time 14:10
 */
@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableDiscoveryClient
//@MapperScan("com.jayud.oms.mapper")
@MapperScan(basePackages = {"com.jayud.**.mapper"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

@EnableResourceServer
@EnableFeignClients(basePackages = "com.jayud")
public class JayudOmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudOmsApplication.class,args); }
}
