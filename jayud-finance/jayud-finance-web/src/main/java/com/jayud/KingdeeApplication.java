package com.jayud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class KingdeeApplication {
    public static void main(String[] args) {
        SpringApplication.run(KingdeeApplication.class, args);
    }

}
