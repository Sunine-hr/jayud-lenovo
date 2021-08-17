package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


/**
 * no_rule
 */
@Getter
@AllArgsConstructor
public enum NoCodeEnum {


    COMMODITY("1001","商品编号"),
    D001("D001","委托单号"),
    ;

    private String code;
    private String desc;

    public static String getDesc(Integer code) {
        for (NoCodeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
