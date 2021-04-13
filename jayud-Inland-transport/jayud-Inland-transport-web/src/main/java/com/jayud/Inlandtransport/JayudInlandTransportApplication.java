package com.jayud.Inlandtransport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 内陆
 * @author larry
 * @Date 2020/4/13
 * @Time 14:10
 */
@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.jayud.Inlandtransport.mapper")
public class JayudInlandTransportApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudInlandTransportApplication.class,args); }
}
