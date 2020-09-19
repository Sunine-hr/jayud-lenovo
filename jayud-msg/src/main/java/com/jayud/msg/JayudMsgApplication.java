package com.jayud.msg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
/**
 * @author william
 * @description
 * @Date: 2020-09-11 14:26
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jayud"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableKafka
public class JayudMsgApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudMsgApplication.class, args);
    }
}
