package com.jayud.oms.security.util;

import com.jayud.common.RedisUtils;
import com.jayud.oms.security.properties.JayudSecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;

/**
 * Security 工具类
 **/
public class SecurityUtil {

    private static final JayudSecurityProperties securityProperties = SpringContextUtil.getBean(JayudSecurityProperties.class);
    private static final RedisUtils redisService = SpringContextUtil.getBean(RedisUtils.class);

    /**
     * 获取用户信息
     *
     * @return
     */
    public static String getUserInfo() {
        if (!StringUtils.isEmpty(securityProperties.getDefaultUserId())) {
            return securityProperties.getDefaultUserId();
        }
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    /**
     * 解析请求头用户ID
     *
     * @return
     */
    public static String getUserId() {
        try {
            //http请求
            HttpServletRequest request = HttpUtil.getHttpRequestServletContext();

            String token = request.getHeader(securityProperties.getHeader());
            if (token != null) {
                String user = Jwts.parser().setSigningKey(securityProperties.getHeaderPrefix().trim()).parseClaimsJws(token.replace(securityProperties.getHeaderPrefix(), ""))
                        .getBody().getSubject();
                if (user != null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user, null, new ArrayList<>());
                    String id = authenticationToken.getPrincipal().toString();
                    // 若token存在，则未过期
                    // 若当前token与缓存中最新的token不相同，则为过期token
                    String tokenKey = (String) redisService.get(securityProperties.getHeaderPrefix().trim() + "_MINI_TOKEN_" + id);
                    if (StringUtils.isEmpty(tokenKey) || !token.replace(securityProperties.getHeaderPrefix(), "").trim().equals(tokenKey)) {
                        return null;
                    }
                    return id;
                }
            }
        } catch (Exception e) {
            System.out.println("该token已过期");
        }
        return null;
    }


    /**
     * 登出
     */
    public static Boolean logout(String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            redisService.delete(securityProperties.getHeaderPrefix().trim() + "_MINI_TOKEN_" + id);
            new SecurityContextLogoutHandler().logout(HttpUtil.getHttpRequestServletContext(),
                    HttpUtil.getHttpResponseServletContext(), auth);
            return true;
        }
        return false;
    }

    /**
     * 生成token
     */
    public static String generateToken(String id) {
        String token = null;
        if (securityProperties.getExpired() == 0) {

            token = Jwts.builder().setSubject(id)
                    .signWith(SignatureAlgorithm.HS512, securityProperties.getHeaderPrefix().trim()).compact();
        } else {
            token = Jwts.builder().setSubject(id)
                    .setExpiration(new Date(System.currentTimeMillis() + (securityProperties.getExpired() * 1000)))
                    .signWith(SignatureAlgorithm.HS512, securityProperties.getHeaderPrefix().trim()).compact();
        }
        return token;
    }

    /**
     * 获取token
     */
    public static String getToken(String id) {
        return (String) redisService.get(securityProperties.getHeaderPrefix().trim() + "_MINI_TOKEN_" + id);
    }


}