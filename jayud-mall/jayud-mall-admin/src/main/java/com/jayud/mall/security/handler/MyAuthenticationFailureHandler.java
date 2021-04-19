package com.jayud.mall.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>自定义登录失败逻辑</p>
 * <p>实现`org.springframework.security.web.authentication.AuthenticationFailureHandler`的`onAuthenticationFailure`方法</p>
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * json字符串处理
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
//        //状态码定义为500（`HttpStatus.INTERNAL_SERVER_ERROR.value()`），即系统内部异常
//        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        response.setContentType("application/json;charset=utf-8");
//        response.getWriter().write(mapper.writeValueAsString(ex.getMessage()));

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        Map<String,Object> map = new HashMap<>();
        map.put("code",401);
        if (ex instanceof UsernameNotFoundException){
            map.put("msg","用户名不存在");
        } else if(ex instanceof BadCredentialsException) {
            map.put("msg","用户名或密码错误");
        } else if (ex instanceof DisabledException) {
            map.put("msg","账户被禁用");
        } else {
            map.put("msg",ex.getMessage());
        }
        map.put("data",null);
        out.write(objectMapper.writeValueAsString(map));
        out.flush();
        out.close();


    }
}
