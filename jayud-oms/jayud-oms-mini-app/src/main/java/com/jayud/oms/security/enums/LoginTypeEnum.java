package com.jayud.oms.security.enums;

import com.jayud.oms.security.strategy.LoginStrategy;
import com.jayud.oms.security.strategy.PwdLoginStrategy;

/**
 * 登陆类型枚举
 */
public enum LoginTypeEnum {

    PASSWORD(1, "密码登录", PwdLoginStrategy.class),
//    CODE(2,"验证码登录", CodeLoginStrategy.class),
//    FACE(3,"人脸登录", FaceLoginStrategy.class)
    ;
    private int code;
    private String desc;
    private Class<?> clazz;

    LoginTypeEnum(int code, String desc, Class<? extends LoginStrategy> clazz) {
        this.code = code;
        this.desc = desc;
        this.clazz = clazz;
    }

    public static Class<?> getClassByCode(Integer code) {
        if (code == null) return null;
        for (LoginTypeEnum value : LoginTypeEnum.values()) {
            if (value.code == code) return value.clazz;
        }
        return null;
    }


    public Class<?> getClazz() {
        return clazz;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
