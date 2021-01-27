//package com.jayud.mall.security.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.savedrequest.RequestCache;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * <p>自定义登录成功逻辑</p>
// * <p>实现`org.springframework.security.web.authentication.AuthenticationSuccessHandler`接口的`onAuthenticationSuccess`方法</p>
// */
//@Component
//public class MyAuthenticationSucessHandler implements AuthenticationSuccessHandler {
//
//    /**
//     * 其中`HttpSessionRequestCache`为Spring Security提供的用于缓存请求的对象，通过调用它的`getRequest`方法可以获取到本次请求的HTTP信息
//     */
//    private RequestCache requestCache = new HttpSessionRequestCache();
//    /**
//     * `DefaultRedirectStrategy`的`sendRedirect`为Spring Security提供的用于处理重定向的方法
//     */
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//    /**
//     * json字符串处理
//     */
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
////        //输出如下JSON信息
////        response.setContentType("application/json;charset=utf-8");
////        response.getWriter().write(mapper.writeValueAsString(authentication));
////
//////        //登录成功后页面将跳转回引发跳转的页面
//////        SavedRequest savedRequest = requestCache.getRequest(request, response);
//////        redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
////
//////        //登录成功指定跳转的页面，比如跳转到`/index`
//////        redirectStrategy.sendRedirect(request, response, "/index");
//
//        Map<String,Object> map = new HashMap<>();
//        map.put("code",200);
//        map.put("msg","登录成功");
//        map.put("data",authentication);
//        response.setContentType("application/json;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        PrintWriter out = response.getWriter();
//        out.write(objectMapper.writeValueAsString(map));
//        out.flush();
//        out.close();
//
//    }
//}
