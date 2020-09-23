package com.jayud.airfreight;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author william
 * @description
 * @Date: 2020-09-14 13:41
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jayud.common","com.jayud.airfreight"})
@MapperScan(basePackages = {"com.jayud.airfreight.mapper"})
public class AirFreightApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirFreightApiApplication.class, args);
    }
}
