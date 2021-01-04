package com.jayud.mall.admin.security.domain;

import java.io.Serializable;

/**
 * 基础权限VO
 */
public class BaseAuthVO implements Serializable {

    private static final long serialVersionUID = 8847125691904516507L;

    /**
     * admin 后台用户
     */
    public static final String ADMIN_USER_LOGIN_SESSION_KEY = "ADMIN_USER_LOGIN_SESSION_KEY";

    /**
     * web C端用户
     */
    public static final String WEB_CUSTOMER_USER_LOGIN_SESSION_KEY = "WEB_CUSTOMER_USER_LOGIN_SESSION_KEY";
}
