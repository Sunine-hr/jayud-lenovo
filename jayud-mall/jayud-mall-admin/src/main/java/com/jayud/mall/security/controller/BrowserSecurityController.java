package com.jayud.mall.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 浏览器安全控制器BrowserSecurityController
 */
@Api(tags = "浏览器安全控制器")
@RestController
public class BrowserSecurityController {

    /**
     * 其中`HttpSessionRequestCache`为Spring Security提供的用于缓存请求的对象，通过调用它的`getRequest`方法可以获取到本次请求的HTTP信息
     */
    private RequestCache requestCache = new HttpSessionRequestCache();
    /**
     * `DefaultRedirectStrategy`的`sendRedirect`为Spring Security提供的用于处理重定向的方法
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @ApiOperation(value = "测试spring security")
    @GetMapping("hello")
    public String hello() {
        return "hello spring security";
    }

    @ApiOperation(value = "登录成功后跳转的地址")
    @GetMapping("index")
    public Object index(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @ApiOperation(value = "登录成功后跳转的地址2(另一种获取authentication的方式)")
    @GetMapping("index2")
    public Object index(Authentication authentication) {
        return authentication;
    }

    @ApiOperation(value = "在未登录的情况下，当用户访问html资源的时候跳转到登录页，否则返回JSON格式数据，状态码为401")
    @GetMapping("/authentication/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html"))
                //在未登录的情况下，当用户访问html资源的时候跳转到登录页
                redirectStrategy.sendRedirect(request, response, "/login.html");
        }
        return "访问的资源需要身份认证！";
    }

    @ApiOperation(value = "session失效")
    @GetMapping("/session/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String sessionInvalid(){
        return "session已失效，请重新认证";
    }

    @ApiOperation(value = "退出登录")
    @GetMapping("/signout/success")
    public String signout() {
        return "退出成功，请重新登录";
    }


}
