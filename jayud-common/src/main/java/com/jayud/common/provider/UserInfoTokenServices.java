package com.jayud.common.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.*;

/**
 * 用户信息返回类，使用在控制类的安全校验，扩展于 {@link org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices}。
 */
public class UserInfoTokenServices implements ResourceServerTokenServices {

	protected final Log logger = LogFactory.getLog(getClass());

	private static final String[] PRINCIPAL_KEYS = new String[] { "user", "username", "userid", "user_id", "login", "id", "name" };

	private static final String ERROR = "error";

	private final String userInfoEndpointUrl;

	private final String clientId;

	private OAuth2RestOperations restTemplate;

	private String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;

	private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

	public UserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
		this.userInfoEndpointUrl = userInfoEndpointUrl;
		this.clientId = clientId;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public void setRestTemplate(OAuth2RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
		this.authoritiesExtractor = authoritiesExtractor;
	}

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken) {
		Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
		if (map.containsKey(ERROR)) {
			this.logger.debug("UserInfo抛出异常：" + map.get(ERROR));
			throw new InvalidTokenException(accessToken);
		}
		return extractAuthentication(map);
	}

	private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
		Object principal = getPrincipal(map);
		OAuth2Request request = getRequest(map);
		Object authoritiesValue = map.get("authorities");
		if (null == authoritiesValue) {
			map.put("authorities", Collections.EMPTY_LIST);
		}
		List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(map);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
		token.setDetails(map);
		return new OAuth2Authentication(request, token);
	}

	private Object getPrincipal(Map<String, Object> map) {
		for (String key : PRINCIPAL_KEYS) {
			if (map.containsKey(key)) {
				return map.get(key);
			}
		}
		return "unknown";
	}

	private OAuth2Request getRequest(Map<String, Object> map) {
		Map<String, Object> request = (Map<String, Object>) map.get("oauth2Request");

		String clientIdValue = (String) request.get("clientId");

		Object object = request.get("scope");
		Set<String> scope;

		if (object instanceof LinkedHashMap) {
			scope = new LinkedHashSet<>(
					request.containsKey("scope") ? (Collection<String>) new ArrayList<String>(Arrays.asList(((LinkedHashMap) object).get("scope").toString()))
							: Collections.<String>emptySet());
		} else {
			scope = new LinkedHashSet<>(request.containsKey("scope") ? (Collection<String>) object : Collections.<String>emptySet());
		}

		return new OAuth2Request(null, clientIdValue, null, true, new HashSet<>(scope), null, null, null, null);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}

	private Map<String, Object> getMap(String path, String accessToken) {
		this.logger.debug("Getting user info from: " + path);
		try {
			OAuth2RestOperations oauth2RestOperations = this.restTemplate;
			if (oauth2RestOperations == null) {
				BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
				resource.setClientId(this.clientId);
				oauth2RestOperations = new OAuth2RestTemplate(resource);
			}
			OAuth2AccessToken existingToken = oauth2RestOperations.getOAuth2ClientContext().getAccessToken();
			if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
				DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
				token.setTokenType(this.tokenType);
				oauth2RestOperations.getOAuth2ClientContext().setAccessToken(token);
			}
			return oauth2RestOperations.getForEntity(path, Map.class).getBody();
		} catch (Exception ex) {
			this.logger.info("Could not fetch user details: " + ex.getClass() + ", " + ex.getMessage());
			return Collections.<String, Object>singletonMap(ERROR, "Could not fetch user details");
		}
	}

}
