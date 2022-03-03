package com.jayud.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * OAuth2安全认证授权启动类
 */
@MapperScan(basePackages = {"com.jayud.**.mapper"})
@ComponentScan(basePackages = {"com.jayud"})
@SpringBootApplication(scanBasePackages = "com.jayud")
@EnableResourceServer
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.jayud")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCaching
public class JayudAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(JayudAuthApplication.class, args);
	}

}
