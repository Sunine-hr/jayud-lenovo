//package com.jayud.mall.security.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * <p>自定义退出登录行为</p>
// */
//@Component
//@Slf4j
//public class MyLogOutSuccessHandler implements LogoutSuccessHandler {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//
////        Object principal = authentication.getPrincipal();
////        String json = JSONObject.toJSONString(principal);
////        log.info("退出登录成功："+principal);
////        System.out.println(json);
//////        String username = (String) principal;
//////        System.out.println("退出成功，用户名：{"+username+"}");
////        // 重定向到登录页
//////        httpServletResponse.sendRedirect("/login.html");
////
////        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
////        httpServletResponse.setContentType("application/json;charset=utf-8");
////        httpServletResponse.getWriter().write("退出成功，请重新登录");
//
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("code",200);
//        map.put("message","退出成功");
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
