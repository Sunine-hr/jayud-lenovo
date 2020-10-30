package com.jayud.common.filter;

import com.alibaba.fastjson.JSONObject;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//@Component
@WebFilter(urlPatterns = "/*", filterName = "userHeaderFilter")
public class UserHeaderFilter implements Filter {

    @Autowired
    RedisUtils redisUtils;

    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/system/user/login","/system/user/logout","/api/")));


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = false;
        for (String url : ALLOWED_PATHS) {
            allowedPath = path.contains(url);
            if(allowedPath){
                break;
            }
        }
        if (allowedPath) {
            System.out.println("这里是不需要处理的url进入的方法");
        }
        else {
            String token = request.getHeader("token");
            String user = redisUtils.get(token,10000l);
            UserOperator.setToken(user);
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
