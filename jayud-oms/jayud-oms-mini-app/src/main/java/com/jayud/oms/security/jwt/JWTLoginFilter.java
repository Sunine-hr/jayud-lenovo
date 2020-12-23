package com.jayud.oms.security.jwt;


import com.jayud.oms.security.enums.MiniResultEnums;
import com.jayud.oms.security.service.LoginOperatingService;
import com.jayud.oms.security.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private LoginOperatingService loginOperatingService;


    public JWTLoginFilter(AuthenticationManager authenticationManager,
                          LoginOperatingService loginOperatingService) {
        this.authenticationManager = authenticationManager;
        this.loginOperatingService=loginOperatingService;
    }

    // 接收并解析用户凭证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        //装载权限管理器
        loginOperatingService.loadAuthenticationManager(this.authenticationManager);
        try {
            //登录接口
            return loginOperatingService.login(req, res);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            HttpUtil.writeError(req, res, MiniResultEnums.getResultEnumsByMsg(e.getMessage()));
            return null;
        }
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) {
        loginOperatingService.successLogin(req,res,chain,auth);
    }
}
