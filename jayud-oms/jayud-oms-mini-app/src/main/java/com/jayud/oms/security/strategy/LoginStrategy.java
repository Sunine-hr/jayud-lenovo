package com.jayud.oms.security.strategy;


import com.jayud.oms.security.entity.Certificate;
import com.jayud.oms.security.entity.LoginUser;

/**
 * 登录策略
 */
public interface LoginStrategy {

      boolean doLogin(LoginUser loginUser, Certificate certificate);
}
