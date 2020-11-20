package com.jayud.oms.security.strategy;

import com.jayud.common.utils.MD5;
import com.jayud.oms.security.entity.Certificate;
import com.jayud.oms.security.entity.LoginUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

/**
 * 密码登录
 */
public class PwdLoginStrategy implements LoginStrategy {


    @Override
    public boolean doLogin(LoginUser loginUser, Certificate certificate) {
        String password = loginUser.getPassword();
        password = MD5.encode(password);
        return password.equals(certificate.getCertificate());
    }
}
