package com.jayud.mall.security.service;

import com.jayud.mall.security.domain.AuthUser;
import com.jayud.mall.security.domain.BaseAuthVO;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * 基本service
 */
@Component
public class BaseService {

    /**
     * 获取HttpSession
     * @return
     */
    public HttpSession getHttpSession() {
        ServletRequestAttributes sa = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return sa != null && sa.getRequest() != null ? sa.getRequest().getSession() : null;
    }

    /**
     * 获取LoginToken
     * @return
     */
    public Object getLoginToken() {
        Object o = null;
        ServletRequestAttributes sa = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (sa != null && sa.getRequest() != null) {
            o = this.getHttpSession().getAttribute(BaseAuthVO.USER_LOGIN_SESSION_KEY);
            return o;
        } else {
            return null;
        }
    }

    /**
     * 获取当前登录User
     * @return
     */
    public AuthUser getUser() {
        Object o = this.getLoginToken();
        return o == null ? null : (AuthUser)o;
    }


}
