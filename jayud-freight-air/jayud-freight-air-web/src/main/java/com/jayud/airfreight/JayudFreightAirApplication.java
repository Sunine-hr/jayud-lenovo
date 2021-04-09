package com.jayud.airfreight;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author larry
 * @Date 2020/4/13
 * @Time 14:10
 */
@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.jayud.airfreight.mapper")
public class JayudFreightAirApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudFreightAirApplication.class,args); }
}
