package com.jayud.oms.security;


import com.jayud.oms.security.filter.JWTAuthenticationFilter;
import com.jayud.oms.security.jwt.CustomAuthenticationProvider;
import com.jayud.oms.security.jwt.JWTLoginFilter;
import com.jayud.oms.security.properties.JayudSecurityProperties;
import com.jayud.oms.security.service.LoginOperatingService;
import com.jayud.oms.security.service.PermissionOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@ConditionalOnProperty(value = "kooko.security.security-switch")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //    @Autowired
//    private UserDetailsService userDetailsService;
    @Autowired
    private JayudSecurityProperties securityProperties;
    @Autowired
    private LoginOperatingService loginOperatingService;
    @Autowired
    private PermissionOperationService permissionOperationService;


    /**
     * 权限配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .authorizeRequests()
//                .antMatchers("/api/swagger-resources/**").permitAll()
//                .antMatchers("/swagger-ui/**").permitAll()
//                .antMatchers("/swagger-ui.html").permitAll()
//                .antMatchers("/swagger-resources/**").permitAll()
//                .antMatchers("/profile/**").permitAll()
//                .antMatchers("/profile/**").permitAll()
//                .antMatchers("/v3/**").permitAll()
                .antMatchers(securityProperties.getPermitAll())
                .permitAll() // 配置拦截规则,允许配置的接口不进行拦截
                .anyRequest().authenticated() // 所有请求需要身份认证
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager(), loginOperatingService))
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), permissionOperationService, securityProperties));
    }

    /**
     * 自定义登录认证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new CustomAuthenticationProvider(loginOperatingService));
    }
}
