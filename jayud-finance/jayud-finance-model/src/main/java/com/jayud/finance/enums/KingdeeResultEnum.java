package com.jayud.finance.enums;


import com.baomidou.mybatisplus.extension.api.IErrorCode;
import lombok.Getter;

/**
 */
@Getter
public enum KingdeeResultEnum {

    UNKNOWN_ERROR(-1, "未知错误"),

    SUCCESS(0, "成功"),

    PARAM_ERROR(1,"参数不正确"),

    EMPTY_PRODUCTS(101, "商品信息为空"),

    PRODUCT_NOT_EXIST(10, "不存在的商品"),

    LOGIN_FAIL(25, "登录失败, 登录信息不正确"),

    LOGOUT_SUCCESS(26, "登出成功"),
    ;

    private Integer code;

    private String message;

    KingdeeResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
