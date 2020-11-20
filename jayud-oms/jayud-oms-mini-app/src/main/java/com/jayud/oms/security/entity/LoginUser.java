package com.jayud.oms.security.entity;

import com.alibaba.druid.util.StringUtils;
import lombok.Data;
import org.apache.shiro.ShiroException;

import java.util.Map;

@Data
public class LoginUser {
    //用户id
    private Long id;
    //账号
    private String account;
    //密码
    private String password;
    //验证码
    private String code;
    //登录类型（1.密码登录，2.验证码登录，3.人脸登录）
    private Integer type;
    //扩展参数
    private Map<String, Object> params;

    public int selectLoginType() {
        if (this.type != null) {
            return this.type;
        }
        //当登录参数有密码时,设置为密码登录类型
        if (this.password != null) {
            this.type = 1;
        }else {
            //当登录参数有验证码时,设置为验证码登录类型
            this.type = 2;
        }
        return this.type;
    }

    /**
     * 校验必填参数
     */
    public void check(){
        if (StringUtils.isEmpty(this.account)) throw new ShiroException("亲！请输入账号");
        if (StringUtils.isEmpty(password)&& StringUtils.isEmpty(code)) throw new ShiroException("亲！请输入密码或者验证码");

    }
}
