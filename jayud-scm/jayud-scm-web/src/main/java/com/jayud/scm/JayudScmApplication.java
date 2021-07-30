package com.jayud.scm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * jayud-scm模块启动类
 * @author
 * @description:
 * @date 2020/10/23 11:23
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.jayud.common","com.jayud.scm"})
public class JayudScmApplication {

    public static void main(String[] args) {
        SpringApplication.run(JayudScmApplication.class,args);
    }

}
