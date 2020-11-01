package com.jayud.mall.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <p>定义浏览器Security配置,继承WebSecurityConfigurerAdapter</p>
 * <p>`WebSecurityConfigurerAdapter`是由Spring Security提供的Web应用安全配置的适配器</p>
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * <p>重写configure方法，并设置自己的步骤</p>
     * <p>想让Security配置不生效，重写configure方法，方法内容为空</p>
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 表单登录
//         http.httpBasic() // HTTP Basic
                .and()
                .authorizeRequests() // 授权配置
                .anyRequest()  // 所有请求
                .authenticated(); // 都需要认证
    }
}
