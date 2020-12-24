package com.jayud.mall.security.controller;

import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.domain.BaseAuthVO;
import com.jayud.mall.admin.security.service.BaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

/**
 * 浏览器安全控制器BrowserSecurityController
 */
@RestController
@Api(tags = "S001-后台-Security安全接口")
@ApiSort(value = 1)
public class BrowserSecurityController {

    /**
     * 其中`HttpSessionRequestCache`为Spring Security提供的用于缓存请求的对象，通过调用它的`getRequest`方法可以获取到本次请求的HTTP信息
     */
    private RequestCache requestCache = new HttpSessionRequestCache();
    /**
     * `DefaultRedirectStrategy`的`sendRedirect`为Spring Security提供的用于处理重定向的方法
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 获取HttpSession
     * @return
     */
    public HttpSession getHttpSession() {
        ServletRequestAttributes sa = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return sa != null && sa.getRequest() != null ? sa.getRequest().getSession() : null;
    }

    /**
     * baseService，获取登录用户
     */
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "测试spring security")
    @ApiOperationSupport(order = 1)
    @GetMapping("hello")
    public String hello() {
        return "hello spring security";
    }

    @ApiOperation(value = "测试")
    @ApiOperationSupport(order = 2)
    @PostMapping(value = "/hi")
    public String hi(){
        return "hi mall admin";
    }

    @ApiOperation(value = "登录成功后跳转的地址")
    @ApiOperationSupport(order = 3)
    @GetMapping("index")
    public Object index(){
        //登录成功，测试获取用户信息
        org.springframework.security.core.userdetails.User myuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<GrantedAuthority> authorities = myuser.getAuthorities();
        String username = myuser.getUsername();
        String password = myuser.getPassword();

        Object token = getHttpSession().getAttribute(BaseAuthVO.ADMIN_USER_LOGIN_SESSION_KEY);
        AuthUser userVO = (AuthUser) token;

        AuthUser user = baseService.getUser();

        return SecurityContextHolder.getContext().getAuthentication();
    }

    @ApiOperation(value = "登录成功后跳转的地址2(另一种获取authentication的方式)")
    @ApiOperationSupport(order = 4)
    @GetMapping("index2")
    public Object index(Authentication authentication) {
        return authentication;
    }

    @ApiOperation(value = "在未登录的情况下，当用户访问html资源的时候跳转到登录页，否则返回JSON格式数据，状态码为401")
    @ApiOperationSupport(order = 5)
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
    @ApiOperationSupport(order = 6)
    @GetMapping("/session/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String sessionInvalid(){
        return "session已失效，请重新认证";
    }

    @ApiOperation(value = "退出登录")
    @ApiOperationSupport(order = 7)
    @GetMapping("/signout/success")
    public String signout() {
        return "退出成功，请重新登录";
    }


}
