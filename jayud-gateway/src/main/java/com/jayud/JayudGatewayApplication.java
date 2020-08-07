package com.jayud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author larry
 * @date 2020/7/10
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
public class JayudGatewayApplication {
    public static void main(String []args){
        SpringApplication.run(JayudGatewayApplication.class,args);
        System.err.println("网关gateway启动了。");
    }
}