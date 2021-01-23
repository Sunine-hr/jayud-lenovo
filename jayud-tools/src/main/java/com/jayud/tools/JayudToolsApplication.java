package com.jayud.tools;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"com.jayud.tools"})
@EnableDiscoveryClient
@EnableFeignClients
public class JayudToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JayudToolsApplication.class, args);
    }
}
