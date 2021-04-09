package com.jayud.oceanship;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author
 * @Date
 * @Time
 */
@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.jayud.oceanship.mapper")
public class JayudOceanShipApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudOceanShipApplication.class,args); }
}
