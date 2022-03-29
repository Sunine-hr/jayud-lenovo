package com.jayud.oms.order;

import com.jayud.common.filter.UserHeaderFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.jayud")
@MapperScan(basePackages = {"com.jayud.**.mapper"})
@EnableWebSecurity
@ComponentScan(basePackages = {"com.jayud"},excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserHeaderFilter.class))
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JayudOmsOrderWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(JayudOmsOrderWebApplication.class, args);
    }

}
