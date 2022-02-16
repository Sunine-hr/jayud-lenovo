package com.jayud.common.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.LinkedHashMap;

/**
 * @author ciro
 * @date 2022/2/16 11:17
 * @description: 当前用户工具类
 */
public class CurrentUserUtil {

    public static String getUsername(){
        String username = ((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication()).getPrincipal().toString();
        return username;
    }

    public static LinkedHashMap getUserDetail(){
        OAuth2Authentication oauth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) oauth2Authentication
                .getUserAuthentication();
        LinkedHashMap principal = (LinkedHashMap)((LinkedHashMap) usernamePasswordAuthenticationToken.getDetails()).get("principal");
        return principal;
    }


}
