package com.jayud.auth.config;



import com.jayud.auth.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * OAuth2????????????????????????
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

	private TokenStore tokenStore = new InMemoryTokenStore();

	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String CLIENT_CREDENTIALS = "client_credentials";
	private static final String SERVER = "server";
	private static final String SECRET = "123456";

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;



	@Autowired
	private UserDetailsServiceImpl userService;
	@Autowired
	RedisConnectionFactory redisConnectionFactory;


	/**
	 * ?????????????????????JDBC??????????????????
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception {
		clientDetailsServiceConfigurer.inMemory().withClient("browser").authorizedGrantTypes(REFRESH_TOKEN, "password").scopes("ui").and()
				.withClient("jayud-system-web").secret(SECRET).authorizedGrantTypes(CLIENT_CREDENTIALS, REFRESH_TOKEN).scopes(SERVER).and()
				.withClient("jayud-crm-web").secret(SECRET).authorizedGrantTypes(CLIENT_CREDENTIALS, REFRESH_TOKEN).scopes(SERVER).and()
				.withClient("jayud-oms-web").secret(SECRET).authorizedGrantTypes(CLIENT_CREDENTIALS, REFRESH_TOKEN).scopes(SERVER).and()
				.withClient("jayud-wms-web").secret(SECRET).authorizedGrantTypes(CLIENT_CREDENTIALS, REFRESH_TOKEN).scopes(SERVER).and()
				.withClient("jayud-oms-order-web").secret(SECRET).authorizedGrantTypes(CLIENT_CREDENTIALS, REFRESH_TOKEN).scopes(SERVER)
				.accessTokenValiditySeconds(60*60);

		clientDetailsServiceConfigurer.withClientDetails(clientDetailsService());
	}

	/**
	 * ??????Authorization Server??????????????????????????????
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer) throws Exception {
		authorizationServerEndpointsConfigurer
				//??????????????????token
//				.tokenStore(tokenStore)
				//redis??????token
				.tokenStore(redisTokenStore())
//				.tokenServices(redisTokenService())
				.tokenGranter(tokenGranter())
				.authenticationManager(authenticationManager)
				.userDetailsService(userService)
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST,
						HttpMethod.OPTIONS, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)
				//??????/oauth/token??????????????????/sysUser/token
				.pathMapping("/oauth/token", "/auth/token")
		;
	}

	/**
	 * ??????token?????????????????????
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer) throws Exception {
		authorizationServerSecurityConfigurer
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()")
				.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}

	/**
	 * ?????????????????????TokenGranter?????????????????????????????????
	 */
	private TokenGranter tokenGranter;
	private UserDetailsService userDetailsService;
	private AuthorizationServerTokenServices tokenServices;
	private AccessTokenConverter accessTokenConverter;
	private boolean reuseRefreshToken = true;
	private TokenEnhancer tokenEnhancer;
	private AuthorizationCodeServices authorizationCodeServices;
	private OAuth2RequestFactory requestFactory;

	private TokenGranter tokenGranter() {
		if (tokenGranter == null) {
			tokenGranter = new TokenGranter() {
				private CompositeTokenGranter delegate;

				@Override
				public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
					if (delegate == null) {
						delegate = new CompositeTokenGranter(getDefaultTokenGranters());
					}
					return delegate.grant(grantType, tokenRequest);
				}
			};
		}
		return tokenGranter;
	}

	private List<TokenGranter> getDefaultTokenGranters() {
		ClientDetailsService clientDetails = clientDetailsService();
		AuthorizationServerTokenServices authorizationServerTokenServices = tokenServices();
		AuthorizationCodeServices defaultAuthorizationCodeServices = authorizationCodeServices();
		OAuth2RequestFactory oauth2RequestFactory = requestFactory();

		List<TokenGranter> tokenGranters = new ArrayList<>();
		tokenGranters.add(
				new AuthorizationCodeTokenGranter(authorizationServerTokenServices, defaultAuthorizationCodeServices, clientDetails, oauth2RequestFactory));
		tokenGranters.add(new RefreshTokenGranter(authorizationServerTokenServices, clientDetails, oauth2RequestFactory));
		ImplicitTokenGranter implicit = new ImplicitTokenGranter(authorizationServerTokenServices, clientDetails, oauth2RequestFactory);
		tokenGranters.add(implicit);
		tokenGranters.add(new ClientCredentialsTokenGranter(authorizationServerTokenServices, clientDetails, oauth2RequestFactory));
		if (authenticationManager != null) {
			tokenGranters
					.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, authorizationServerTokenServices, clientDetails, oauth2RequestFactory));
		}

//		tokenGranters.add(new SmsTokenGranter(authorizationServerTokenServices, clientDetails, oauth2RequestFactory, authMapper, redisUtils));

		return tokenGranters;
	}

	private ClientDetailsService clientDetailsService() {
		return new ClientDetailsService() {
			@Override
			public ClientDetails loadClientByClientId(String clientId) {
				BaseClientDetails details = new BaseClientDetails();
				details.setClientId(clientId);
				details.setAuthorizedGrantTypes(Arrays.asList(REFRESH_TOKEN, "password", CLIENT_CREDENTIALS, "authorization_code", "implicit", "mobile"));
				details.setScope(Arrays.asList("ui", SERVER));
				return details;
			}
		};
	}

	private void addUserDetailsService(DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
		if (userDetailsService != null) {
			PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
			provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(userDetailsService));
			tokenServices.setAuthenticationManager(new ProviderManager(Arrays.<AuthenticationProvider>asList(provider)));
		}
	}

	private AuthorizationServerTokenServices tokenServices() {
		if (tokenServices != null) {
			return tokenServices;
		}
		//??????redis-token??????
		this.tokenServices = redisTokenService();
		return tokenServices;
	}

	//??????????????????token
	private DefaultTokenServices createDefaultTokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStores());
		defaultTokenServices.setSupportRefreshToken(true);
		defaultTokenServices.setReuseRefreshToken(reuseRefreshToken);
		defaultTokenServices.setClientDetailsService(clientDetailsService());
		defaultTokenServices.setTokenEnhancer(tokenEnhancer());
		addUserDetailsService(defaultTokenServices, this.userDetailsService);
		return defaultTokenServices;
	}

	private TokenStore tokenStores() {
		if (tokenStore == null) {
			if (accessTokenConverter() instanceof JwtAccessTokenConverter) {
				this.tokenStore = new JwtTokenStore((JwtAccessTokenConverter) accessTokenConverter());
			} else {
				this.tokenStore = new InMemoryTokenStore();
			}
		}
		return this.tokenStore;
	}

	private AccessTokenConverter accessTokenConverter() {
		if (this.accessTokenConverter == null) {
			accessTokenConverter = new DefaultAccessTokenConverter();
		}
		return this.accessTokenConverter;
	}

	private TokenEnhancer tokenEnhancer() {
		if (this.tokenEnhancer == null && accessTokenConverter() instanceof JwtAccessTokenConverter) {
			tokenEnhancer = (TokenEnhancer) accessTokenConverter;
		}
		return this.tokenEnhancer;
	}

	private AuthorizationCodeServices authorizationCodeServices() {
		if (authorizationCodeServices == null) {
			authorizationCodeServices = new InMemoryAuthorizationCodeServices();
		}
		return authorizationCodeServices;
	}

	private OAuth2RequestFactory requestFactory() {
		if (requestFactory != null) {
			return requestFactory;
		}
		requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService());
		return requestFactory;
	}

//	@Bean
//	TokenStore tokenStore() {
//		return new RedisTokenStore(redisConnectionFactory);
//	}

	@Bean
	public TokenStore redisTokenStore() {
		RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
		redisTokenStore.setPrefix("auth-token:");
		//????????????token??????
		redisTokenStore.setAuthenticationKeyGenerator(new MyAuthenticationKeyGenerator());
		return redisTokenStore;
	}

	@Bean
	public DefaultTokenServices redisTokenService() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		//??????token??????
		tokenServices.setTokenStore(redisTokenStore());
		//????????????refresh_token???????????????????????????????????????????????????????????????????????????????????????????????????token?????????????????????????????????redis??????token??????
		tokenServices.setSupportRefreshToken(true);
		//??????refresh_token
		tokenServices.setReuseRefreshToken(true);
		//token??????????????????1??????
		tokenServices.setAccessTokenValiditySeconds(24*60*60);
		//refresh_token??????????????????1???
		tokenServices.setRefreshTokenValiditySeconds(36*60*60);
		return tokenServices;
	}
}
