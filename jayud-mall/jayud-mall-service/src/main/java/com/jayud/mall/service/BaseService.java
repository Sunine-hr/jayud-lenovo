package com.jayud.mall.service;

import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.model.vo.domain.BaseAuthVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * admin项目-基本service
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

    /**admin**/

    /**
     * 获取admin LoginToken
     * @return
     */
    public Object getLoginToken() {
        Object o = null;
        ServletRequestAttributes sa = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (sa != null && sa.getRequest() != null) {
            o = this.getHttpSession().getAttribute(BaseAuthVO.ADMIN_USER_LOGIN_SESSION_KEY);
            return o;
        } else {
            return null;
        }
    }

    /**
     * 获取admmin 当前登录User
     * @return
     */
    public AuthUser getUser() {
        Object o = this.getLoginToken();
        return o == null ? null : (AuthUser)o;
    }

    /**web**/

    /**
     * 获取web LoginToken
     * @return
     */
    public Object getWebLoginToken() {
        Object o = null;
        ServletRequestAttributes sa = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (sa != null && sa.getRequest() != null) {
            o = this.getHttpSession().getAttribute(BaseAuthVO.WEB_CUSTOMER_USER_LOGIN_SESSION_KEY);
            return o;
        } else {
            return null;
        }
    }

    /**
     * 获取web 当前登录User
     * @return
     */
    public CustomerUser getCustomerUser() {
        Object o = this.getWebLoginToken();
        return o == null ? null : (CustomerUser)o;
    }


}
