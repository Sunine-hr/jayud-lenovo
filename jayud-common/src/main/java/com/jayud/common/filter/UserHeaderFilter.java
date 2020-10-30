package com.jayud.common.filter;

import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*", filterName = "userHeaderFilter")
public class UserHeaderFilter implements Filter {

    @Autowired
    RedisUtils redisUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("token");
        String user = redisUtils.get(token,10000l);
        UserOperator.setToken(user);
    }

    @Override
    public void destroy() {

    }
}
