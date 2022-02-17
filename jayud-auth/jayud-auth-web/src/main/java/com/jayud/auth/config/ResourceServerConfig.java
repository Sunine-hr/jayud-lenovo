//package com.jayud.auth.config;
//
//import com.jyd.component.commons.config.CommonResourceServerConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.core.session.SessionRegistryImpl;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//
///**
// * 系统管理OAuth2资源服务器
// */
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends CommonResourceServerConfig {
//
//	@Autowired
//	public ResourceServerConfig(ResourceServerProperties sso) {
//		super(sso);
//	}
//
//	/**
//	 * 配置拦截路径的安全规则
//	 */
//	@Override
//	public void configure(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.authorizeRequests().antMatchers("/deviceAuthorization/activate","/deviceAuthorization/license/generateCode").permitAll().anyRequest().authenticated();
//	}
//
//	/**
//	 * 实例化会话监听
//	 *
//	 * @return
//	 */
//	@Bean
//	public SessionRegistry sessionRegistry() {
//		return new SessionRegistryImpl();
//	}
//
//}
