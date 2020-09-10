package com.jayud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.alibaba.nacos.NacosConfigAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author william
 * @description
 * @Date: 2020-09-07 15:22
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
