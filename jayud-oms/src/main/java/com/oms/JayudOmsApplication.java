package com.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author xiaoy
 * @Date 2020/4/13
 * @Time 14:10
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAspectJAutoProxy(exposeProxy = true)
public class JayudOmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(com.oms.JayudOmsApplication.class,args);
        System.err.println("Guarantee服务启动了");
    }
}
