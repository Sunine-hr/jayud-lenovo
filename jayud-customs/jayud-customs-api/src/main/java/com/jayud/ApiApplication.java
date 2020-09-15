package com.jayud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author william
 * @description
 * @Date: 2020-09-07 15:22
 */

@SpringBootApplication()
@MapperScan("com.jayud.mapper")
@EnableFeignClients()
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
