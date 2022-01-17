package com.jayud.scm.model.vo;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户登录token
 * @author chuanmei
 */
@Data
public class UserLoginToken extends UsernamePasswordToken {

    /**
     * 服务端图片验证码
     */
    //private String servicePicAuthCode;

    /**
     *  客户端图片验证码
     */
    //private String clientPickAuthCode;
}
