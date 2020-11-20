package com.jayud.oms.security.service.impl;

import com.jayud.common.RedisUtils;
import com.jayud.oms.security.enums.MiniResultEnums;
import com.jayud.oms.security.properties.JayudSecurityProperties;
import com.jayud.oms.security.service.PermissionOperationService;
import com.jayud.oms.security.util.HttpUtil;
import com.jayud.oms.security.util.SecurityUtil;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 权限操作
 */
public abstract class AbstractPermissionOperationServiceImpl implements PermissionOperationService {

    @Autowired
    private RedisUtils redisService;
    @Autowired
    private JayudSecurityProperties securityProperties;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain, Set<String> allowAccessPaths) throws IOException, ServletException {

        String requestPath = request.getServletPath();

        for (String allowAccessPath : allowAccessPaths) {
            //替换通配符
            allowAccessPath = allowAccessPath.replace("**", ".*");
            if (Pattern.matches("^" + allowAccessPath, requestPath) || "/**".equals(allowAccessPath)) {
                chain.doFilter(request, response);
                return;
            }
        }
        //获取头部token
        String token = request.getHeader(securityProperties.getHeader());
        if (token == null) {
            HttpUtil.writeError(request, response, MiniResultEnums.NOT_LOGGED_IN);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            if (authentication == null) {
                HttpUtil.writeError(request, response, MiniResultEnums.NOT_LOGGED_IN);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            HttpUtil.writeError(request, response, MiniResultEnums.NOT_LOGGED_IN);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * 获取权限
     *
     * @param request
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(securityProperties.getHeader());
        String id = Jwts.parser().setSigningKey(securityProperties.getHeaderPrefix().trim()).parseClaimsJws(token.replace(securityProperties.getHeaderPrefix(), ""))
                .getBody().getSubject();
        if (id != null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, null, new ArrayList<>());
            //自定校验
            this.customCheck(id);
            //若当前token与缓存中最新的token不相同，则为过期token
            String tokenKey = SecurityUtil.getToken(id);
            if (StringUtils.isEmpty(tokenKey) || !token.replace(securityProperties.getHeaderPrefix(), "").trim().equals(tokenKey)) {
                return null;
            }
            return authenticationToken;
        }
        return null;
    }


    /**
     * 自定校验
     */
    public abstract void customCheck(String id) throws SecurityException;

}
