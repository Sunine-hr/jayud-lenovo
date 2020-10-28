package com.jayud.tools;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.jayud.common","com.jayud.tools"})
/*@SpringBootApplication(scanBasePackages = "com.jayud.tools")
@EnableFeignClients()*/
public class JayudToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JayudToolsApplication.class, args);
    }
}
