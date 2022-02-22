package com.jayud.common.utils;

import com.jayud.common.dto.AuthUserDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.LinkedHashMap;

/**
 * @author ciro
 * @date 2022/2/16 11:17
 * @description: 当前用户工具类
 */
public class CurrentUserUtil {

    /**
     * @description 获取用户名称
     * @author  ciro
     * @date   2022/2/21 17:55
     * @param:
     * @return: java.lang.String
     **/
    public static String getUsername(){
        AuthUserDetail authUserDetail = getUserDetail();
        return authUserDetail.getUsername();
    }

    /**
     * @description 获取用户信息
     * @author  ciro
     * @date   2022/2/21 17:56
     * @param:
     * @return: com.jayud.common.dto.AuthUserDetail
     **/
    public static AuthUserDetail getUserDetail(){
        AuthUserDetail authUserDetail = (AuthUserDetail)(((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
        return authUserDetail;
    }

    /**
     * @description 获取当前登录用户token
     * @author  ciro
     * @date   2022/2/21 17:56
     * @param:
     * @return: java.lang.String
     **/
    public static String getUserToken(){
        OAuth2Authentication oauth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        return ((OAuth2AuthenticationDetails) oauth2Authentication.getDetails()).getTokenValue();
    }

    /**
     * @description 获取当前用户租户编码
     * @author  ciro
     * @date   2022/2/22 15:10
     * @param:
     * @return: java.lang.String
     **/
    public static String getUserTenantCode(){
        return getUserDetail().getTenantCode();
    }


}
