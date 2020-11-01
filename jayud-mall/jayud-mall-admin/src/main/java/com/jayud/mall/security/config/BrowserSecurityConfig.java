package com.jayud.mall.security.config;

import com.jayud.mall.security.handler.MyAuthenticationFailureHandler;
import com.jayud.mall.security.handler.MyAuthenticationSucessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>定义浏览器Security配置,继承WebSecurityConfigurerAdapter</p>
 * <p>`WebSecurityConfigurerAdapter`是由Spring Security提供的Web应用安全配置的适配器</p>
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义登录成功逻辑
     */
    @Autowired
    private MyAuthenticationSucessHandler authenticationSucessHandler;
    /**
     * 自定义登录失败逻辑
     */
    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    /**
     * <p>配置PasswordEncoder</p>
     * <p>该对象用于密码加密</p>
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * <p>重写configure方法，并设置自己的步骤</p>
     * <p>想让Security配置不生效，重写configure方法，方法内容为空</p>
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 表单登录
                // http.httpBasic() // HTTP Basic
                .loginPage("/authentication/require") // 登录跳转 URL
                .loginProcessingUrl("/login") // 处理表单登录 URL
                .successHandler(authenticationSucessHandler) // 处理登录成功
                .failureHandler(authenticationFailureHandler) // 处理登录失败
            .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/authentication/require",
                        "/login.html","/css/login.css").permitAll() // 登录跳转 URL 无需认证
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
            .and()
                .csrf().disable();

    }
}
