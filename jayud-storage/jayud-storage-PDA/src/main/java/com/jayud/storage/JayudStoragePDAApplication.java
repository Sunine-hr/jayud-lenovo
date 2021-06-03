package com.jayud.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LLJ
 * @description
 * @Date: 2020-09-14 13:41
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jayud.common","com.jayud.storage"})
@MapperScan(basePackages = {"com.jayud.storage.mapper"})
@EnableDiscoveryClient
@EnableFeignClients
public class JayudStoragePDAApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudStoragePDAApplication.class, args);
    }
}
