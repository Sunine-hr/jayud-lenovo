package com.jayud.common.utils;

import com.alibaba.fastjson.JSON;
import com.jayud.common.dto.AuthUserDetail;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Collection;
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
        LinkedHashMap users = (LinkedHashMap) ((LinkedHashMap)((Authentication)((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication()).getUserAuthentication()).getDetails()).get("principal");
        AuthUserDetail authUserDetail = new AuthUserDetail();
        if (users == null){
            authUserDetail = (AuthUserDetail)(((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
        }else {
            authUserDetail = JSON.parseObject(JSON.toJSONString(users), AuthUserDetail.class);
        }
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

    /**
     * @description 判断是否有角色权限
     * @author  ciro
     * @date   2022/2/24 11:36
     * @param: roleCode
     * @return: java.lang.Boolean
     **/
    public static Boolean hasRole(String roleCode){
        Boolean isHas = false;
        OAuth2Authentication oauth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Collection<GrantedAuthority> collection = oauth2Authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : collection){
            if (roleCode.equals(grantedAuthority.getAuthority())){
                isHas = true;
                break;
            }
        }
        return isHas;
    }

    /**
     * @description 获取当前用户id
     * @author  ciro
     * @date   2022/3/4 15:49
     * @param:
     * @return: java.lang.Long
     **/
    public static Long getUserId(){
        AuthUserDetail authUserDetail = getUserDetail();
        return authUserDetail.getId();
    }

    /**
     * @description 获取真实名称
     * @author  ciro
     * @date   2022/3/9 10:50
     * @param:
     * @return: java.lang.String
     **/
    public static String getUserRealName(){
        AuthUserDetail authUserDetail = getUserDetail();
        return authUserDetail.getRealName();
    }

    /**
    * @description 获取工作台编码
    * @author  ciro
    * @date   2022/3/16 15:02:40
    * @return: java.lang.String
    **/
    public static String getCurrrentUserWorkbenchCode(){
        AuthUserDetail authUserDetail = getUserDetail();
        return authUserDetail.getWorkbenchCode();
    }


}
