package com.jayud.scm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author william
 * @description
 * @Date: 2020-09-14 13:41
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jayud.common","com.jayud.scm"})
@MapperScan(basePackages = {"com.jayud.scm.mapper"})
@EnableDiscoveryClient
@EnableFeignClients
public class JayudScmApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudScmApiApplication.class, args);
    }
}
