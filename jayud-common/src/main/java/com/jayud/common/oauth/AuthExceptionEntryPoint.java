package com.jayud.common.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayud.common.BaseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ciro
 * @date 2022/2/26 10:30
 * @description:    token过期自定义返回
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws ServletException {
        Map<String, Object> map = new HashMap<String, Object>();
        Throwable cause = authException.getCause();
        BaseResult baseResult = new BaseResult();
        if(cause instanceof InvalidTokenException) {
            baseResult = BaseResult.unAuthorized(authException.getMessage());
        }else{
            map.put("code", 4444);//401
            map.put("msg", "访问此资源需要完全的身份验证");
        }
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), baseResult);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
