package com.jayud.customs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author william
 * @description
 * @Date: 2020-09-07 15:22
 */

@SpringBootApplication()
@ComponentScan(basePackages = {"com.jayud.common"})
@EnableFeignClients()
public class JayudCustomsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudCustomsApiApplication.class, args);
    }
}
