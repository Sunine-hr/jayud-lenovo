package com.jayud.oms.security.enums;

import com.jayud.common.enums.IResultCode;

/**
 * @ProjectName ecp_app
 * @Description: TODO
 * @Author hsw
 * @Date 2019/8/28
 * @Version V1.0
 **/
public enum MiniResultEnums implements IResultCode {

    SUCCESS(200, "成功-success"),
    ERROR(500, "系统繁忙"),

    ERROR_ACCOUNT_OR_PASSWORD(8000, "账号或密码错误"),
    VERIFICATION_CODE_ERROR(8001, "验证码错误"),
    NOT_LOGGED_IN(8002, "请先登录"),
    VERIFICATION_CODE_EXPIRED(8003, "短信验证码错误或已过期"),
    USER_HAS_BEEN_DISABLED(8004, "该用户已被禁用请联系客服"),
    PARAM_ERROR(8005, "业务参数错误"),
    ACCOUNT_EXIST(8006, "账号已存在"),
    LOGOUT_FAILED(8007, "登出失败"),
    PLEASE_INPUT_ACCOUNT(8008, "亲！请输入账号"),
    PLEASE_INPUT_PASSWORD(8009, "亲！请输入密码或者验证码"),
    ;


    private Integer code;
    private String msg;

    MiniResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

//    @Override
//    public String getCode() {
//        return code;
//    }
//
//    @Override
//    public String getMessage() {
//        return null;
//    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static MiniResultEnums getResultEnumsByMsg(String msg) {
        for (MiniResultEnums enums : MiniResultEnums.values()) {
            if (enums.getMsg().equals(msg)) {
                return enums;
            }
        }
        return MiniResultEnums.ERROR;
    }


    public static MiniResultEnums getResultEnumsByCode(String code) {
        for (MiniResultEnums enums : MiniResultEnums.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return MiniResultEnums.ERROR;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
