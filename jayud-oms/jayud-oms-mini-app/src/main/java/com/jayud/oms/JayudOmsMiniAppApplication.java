package com.jayud.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.jayud.oms.security","com.jayud.common",
        "com.jayud.oms.service","com.jayud.oms.mapper"})
@EnableDiscoveryClient
@EnableFeignClients
public class JayudOmsMiniAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudOmsMiniAppApplication.class, args);
    }
}
