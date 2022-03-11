package com.jayud.oms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web安全配置类
 */
@Configuration
//@EnableWebSecurity  //启动web安全性
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    /**
     * 为特定的Http请求配置基于Web的安全约束
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests()
//                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//                .antMatchers("/**","/deviceAuthorization/activate","/api/uaa/deviceAuthorization/license/generateCode").permitAll()
//                .anyRequest().authenticated().and().csrf().disable();
        httpSecurity.authorizeRequests().anyRequest().authenticated().and().csrf().disable();
    }


    /**
     * 为特定的Http请求配置基于Web的安全约束
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/initCostTypeByCostInfoCode","/swagger-ui/**", "/swagger-ui.html/**", "/webjars/springfox-swagger-ui/**",
                "/swagger-resources/**", "/v2/api-docs/**",
                "/api/initVehicleSize","/api/initCostUnit","/api/initCost",
                "/api/initCostType","/api/adrrTree","/api/initCurrencyInfo");
    }

}
