package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum NoCodeEnum {


    COMMODITY("1001","商品编号"),
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
