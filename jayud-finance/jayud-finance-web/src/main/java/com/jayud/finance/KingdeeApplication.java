package com.jayud.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.jayud"})
@EnableDiscoveryClient
@MapperScan("com.jayud.finance.mapper")
public class KingdeeApplication {
    public static void main(String[] args) {
        SpringApplication.run(KingdeeApplication.class, args);
    }

}
