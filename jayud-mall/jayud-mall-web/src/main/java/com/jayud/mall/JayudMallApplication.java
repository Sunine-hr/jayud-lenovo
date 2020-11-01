package com.jayud.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * jayud-mall模块启动类(C端，客户端)
 * @author mfc
 * @description:
 * @date 2020/10/23 11:23
 */
@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableDiscoveryClient
@EnableFeignClients
public class JayudMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(JayudMallApplication.class,args);
    }

}