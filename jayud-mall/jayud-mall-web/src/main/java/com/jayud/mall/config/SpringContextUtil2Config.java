package com.jayud.mall.config;

import com.jayud.mall.utils.SpringContextUtil2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置SpringContextUtil2的bean
 */
@Configuration
public class SpringContextUtil2Config {

    @Bean
    public SpringContextUtil2 getSpringUtil2() {
        return new SpringContextUtil2();
    }

}
