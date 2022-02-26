package com.jayud.auth.config;


import com.jayud.auth.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Web安全配置类
 */
@Configuration
@EnableWebSecurity  //启动web安全性
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userService;

    /**
     * 为特定的Http请求配置基于Web的安全约束
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().authorizeRequests().anyRequest().authenticated().antMatchers("/auth/token").permitAll()
                .and().csrf().disable();
    }

    /**
     * 配置认证信息
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 实例化AuthenticationManager对象，以处理认证请求
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 为特定的Http请求配置基于Web的安全约束
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/deviceAuthorization/getActivationInfo",
                "/deviceAuthorization/getDeviceStatus", "/deviceAuthorization/activate",
                "/deviceAuthorization/license/generateCode",
                "/swagger-ui.html", "/swagger-resources/**", "/swagger/**",
                "/**/v2/api-docs", "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.ico", "/ doc.html");
    }

}
