package com.jayud.auth.config;


import com.jayud.common.oauth.AuthExceptionEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 系统管理OAuth2资源服务器
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {

	/**
	 * 配置拦截路径的安全规则
	 */
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().antMatchers("/deviceAuthorization/activate","/deviceAuthorization/license/generateCode").permitAll().anyRequest().authenticated();
	}

	/**
	 * 实例化会话监听
	 *
	 * @return
	 */
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

    @Override
    public void configure(ResourceServerSecurityConfigurer resource) throws Exception {
        //这里把自定义异常加进去
        resource.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }
}
