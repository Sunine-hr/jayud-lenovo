package com.jayud.mall.security.config;

import com.jayud.mall.security.handler.MyAuthenticationFailureHandler;
import com.jayud.mall.security.handler.MyAuthenticationSucessHandler;
import com.jayud.mall.security.handler.MyLogOutSuccessHandler;
import com.jayud.mall.security.service.UserDetailService;
import com.jayud.mall.security.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

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
     * 自定义退出登录行为
     */
    @Autowired
    private MyLogOutSuccessHandler logOutSuccessHandler;
    /**
     * 验证码校验过滤器
     */
    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    /**
     * 自定义用户认证
     */
    @Autowired
    private UserDetailService userDetailService;
    /**
     * 数据源(DataSource)
     */
    @Autowired
    private DataSource dataSource;

    /**
     * <p>配置PasswordEncoder</p>
     * <p>该对象用于密码加密</p>
     * <p>我这里使用BCryptPasswordEncoder，如果有需要用自定义MD5加密，自定义加密算法，实现PasswordEncoder</p>
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * <p>持续的令牌库,将令牌存在数据库中</p>
     * <p>数据库表：persistent_logins</p>
     * <p>查看`JdbcTokenRepositoryImpl`的源码，可以看到其包含了一个`CREATE_TABLE_SQL`属性</p>
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    /**
     * <p>重写configure方法，并设置自己的步骤</p>
     * <p>想让Security配置不生效，重写configure方法，方法内容为空</p>
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        //配置不需要登录验证(开发时)
//        http.authorizeRequests()
//                .anyRequest()
//                .permitAll()
//            .and()
//                .logout()
//                .permitAll()
//            .and()
//                .csrf().disable();

        //配置需要http验证
        http
            //.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加验证码校验过滤器
            .formLogin() // 表单登录
                //使用数据库,注释下面的内容
                //.usernameParameter("username") /* 默认值 username */
                //.passwordParameter("password") /* 默认值 password */
                .loginPage("/authentication/require") // 登录跳转 URL
                .loginProcessingUrl("/login") // 处理表单登录 URL
                .successHandler(authenticationSucessHandler) // 处理登录成功
                .failureHandler(authenticationFailureHandler) // 处理登录失败
            .and()
                .rememberMe() // 添加记住我功能
                .tokenRepository(persistentTokenRepository()) // 配置 token 持久化仓库
                .tokenValiditySeconds(3600) // remember 过期时间，单为秒 有效期一个小时
                .userDetailsService(userDetailService) // 处理自动登录逻辑
            .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/authentication/require",
                        "/login.html","/css/login.css",
                        "/code/image",
                        "/login",
                        "/session/invalid",
                        "/signout/success",
                        "/phonemessages/sendMessage").permitAll() // 登录跳转 URL 无需认证
                //Security,放行swagger2资源,和swagger-bootstrap-ui增强功能
                .antMatchers("/doc.html").permitAll() //增强功能
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/v2/api-docs-ext").permitAll() //增强功能
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
            .and()
                .sessionManagement() // 添加 Session管理器
                .invalidSessionUrl("/session/invalid") // Session失效后跳转到这个链接
            .and()
                .logout() // 配置退出登录
                .logoutUrl("/signout")
                //.logoutSuccessUrl("/signout/success")
                .logoutSuccessHandler(logOutSuccessHandler) // 除了指定logoutUrl外，我们也可以通过logoutSuccessHandler指定退出成功处理器来处理退出成功后的逻辑
                .deleteCookies("JSESSIONID") // 清除 名为 JSESSIONID 的 cookie
            .and()
                .csrf().disable();

    }

    /**
     * 配置Spring Security 用户名密码认证
     * <p>passwordEncoder()使用的是BCryptPasswordEncoder</p>
     * <p>这里可以写自己的加密算法</p>
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());

    }

}
