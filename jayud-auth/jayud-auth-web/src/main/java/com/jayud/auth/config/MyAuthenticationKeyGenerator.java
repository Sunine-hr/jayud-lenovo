package com.jayud.auth.config;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.config.AuthConfig;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author ciro
 * @date 2022/2/23 13:23
 * @description:    oauth2登录token校验
 */
public class MyAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {


    private static final String SCOPE = "scope";

    private static final String USERNAME = "username";

    //是否单点登录
    private Boolean isOnly = true;

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        if (!authentication.isClientOnly()) {
            values.put(USERNAME, authentication.getName());
        }
        if (authorizationRequest.getScope() != null) {
            values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<String>(authorizationRequest.getScope())));
        }

        if (!isOnly){
            values.put("date",DateUtil.now());
        }
        return generateKey(values);
    }
}
