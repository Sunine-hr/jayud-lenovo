package com.jayud.oms.security.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginOperatingService {


    void loadAuthenticationManager(AuthenticationManager authenticationManager);

    /**
     * 登录
     */
    Authentication login(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 登录校验
     * @return
     */
    Authentication checkLogin(Authentication authentication);

    /**
     * 成功登录
     * @param req
     * @param res
     * @param chain
     * @param auth
     */
    void successLogin(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth);
}
