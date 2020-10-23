package com.jayud.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * jayud-mall模块启动类(后端，管理员使用)
 * @author mfc
 * @description:
 * @date 2020/10/23 11:29
 */
@SpringBootApplication(scanBasePackages = "com.jayud.mall")
@EnableFeignClients()
public class JayudMallAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(JayudMallAdminApplication.class, args);
    }
}
