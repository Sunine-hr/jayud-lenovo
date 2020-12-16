package com.jayud.mall.security.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>自定义退出登录行为</p>
 */
@Component
@Slf4j
public class MyLogOutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        String json = JSONObject.toJSONString(principal);
        log.info("退出登录成功："+principal);
        System.out.println(json);
//        String username = (String) principal;
//        System.out.println("退出成功，用户名：{"+username+"}");
        // 重定向到登录页
//        httpServletResponse.sendRedirect("/login.html");

        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write("退出成功，请重新登录");
    }
}
