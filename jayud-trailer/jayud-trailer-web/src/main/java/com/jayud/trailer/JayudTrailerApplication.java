package com.jayud.trailer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author
 * @Date
 * @Time
 */
@SpringBootApplication(scanBasePackages = "com.jayud",exclude= {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
public class JayudTrailerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JayudTrailerApplication.class,args); }
}
