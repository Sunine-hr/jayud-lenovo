package com.jayud.oms.security.jwt;


import com.jayud.oms.security.service.LoginOperatingService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 自定义身份认证验证组件
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {


    private LoginOperatingService loginOperatingService;

    public CustomAuthenticationProvider(LoginOperatingService loginOperatingService) {
        this.loginOperatingService=loginOperatingService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return loginOperatingService.checkLogin(authentication);
    }

    // 是否可以提供输入类型的认证服务
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
