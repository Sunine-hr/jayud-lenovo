package com.jayud.mall.security.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//默认密码处理
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

    public DefaultPasswordEncoder() {
        this(-1);
    }
    public DefaultPasswordEncoder(int strength) {
    }
//    //进行MD5加密
//    @Override
//    public String encode(CharSequence charSequence) {
//        return MD5.encrypt(charSequence.toString());
//    }
//    //进行MD5密码比对
//    @Override
//    public boolean matches(CharSequence charSequence, String encodedPassword) {
//        return encodedPassword.equals(MD5.encrypt(charSequence.toString()));
//    }

    //进行BCryptPasswordEncoder加密
    @Override
    public String encode(CharSequence charSequence) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(charSequence);
    }
    //进行BCryptPasswordEncoder密码比对
    @Override
    public boolean matches(CharSequence charSequence, String encodedPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(charSequence,encodedPassword);
    }

}
