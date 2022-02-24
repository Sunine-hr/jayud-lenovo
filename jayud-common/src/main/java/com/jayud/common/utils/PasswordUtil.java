package com.jayud.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ciro
 * @date 2022/2/23 14:18
 * @description: 密码工具类
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * @description 加密Oauth2密码
     * @author  ciro
     * @date   2022/2/23 14:19
     * @param: password
     * @return: java.lang.String
     **/
    public static String encodeOauthPassword(String password){
        return encoder.encode(password);
    }

}
