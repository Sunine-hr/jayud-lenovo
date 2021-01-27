package com.jayud.mall.security.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayud.mall.model.vo.domain.BaseAuthVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
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
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 浏览器安全控制器BrowserSecurityController
 */
@RestController
@Api(tags = "C001-C端-Security安全接口")
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

    @Autowired
    private ObjectMapper objectMapper;

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
        return "hi mall web";
    }

    @ApiOperation(value = "登录成功后跳转的地址")
    @ApiOperationSupport(order = 3)
    @GetMapping("index")
    public Object index(){
        //登录成功，测试获取用户信息
        User myuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<GrantedAuthority> authorities = myuser.getAuthorities();
        String username = myuser.getUsername();
        String password = myuser.getPassword();

        Object token = getHttpSession().getAttribute(BaseAuthVO.WEB_CUSTOMER_USER_LOGIN_SESSION_KEY);
        CustomerUser userVO = (CustomerUser) token;

        CustomerUser user = baseService.getCustomerUser();

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
    @ResponseStatus(HttpStatus.UNAUTHORIZED) //UNAUTHORIZED(401, "Unauthorized"),
    public void requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        SavedRequest savedRequest = requestCache.getRequest(request, response);
//        if (savedRequest != null) {
//            String targetUrl = savedRequest.getRedirectUrl();
//            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html"))
//                //在未登录的情况下，当用户访问html资源的时候跳转到登录页
//                redirectStrategy.sendRedirect(request, response, "/login.html");
//        }

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        Map<String,Object> map = new HashMap<>();
        map.put("code",401);
        map.put("msg","未登录,访问的资源需要身份认证！");
        map.put("data", null);
        out.write(objectMapper.writeValueAsString(map));
        out.flush();
        out.close();
    }

    @ApiOperation(value = "session失效")
    @ApiOperationSupport(order = 6)
    @GetMapping("/session/invalid")
    @ResponseStatus(HttpStatus.FORBIDDEN) //FORBIDDEN(403, "Forbidden"),
    public void sessionInvalid(HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Map<String,Object> map = new HashMap<>();
            map.put("code",403);
            map.put("msg","session已失效，请重新认证");
            map.put("data",null);
            out.write(objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }

    @ApiOperation(value = "退出登录")
    @ApiOperationSupport(order = 7)
    @GetMapping("/signout/success")
    public String signout() {
        return "退出成功，请重新登录";
    }


}
