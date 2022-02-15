package com.jayud.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author ciro
 * @date 2022/2/15 11:07
 * @description:
 */
@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.jayud.system.mapper")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JayudSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudSystemApplication.class, args);
    }
}
