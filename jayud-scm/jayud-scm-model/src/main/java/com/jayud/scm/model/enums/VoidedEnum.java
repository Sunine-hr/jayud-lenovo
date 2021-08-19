package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 废弃标志
 * */
@Getter
@AllArgsConstructor
public enum VoidedEnum {

    ZERO(0,"未标记"),
    ONE(1,"已标记"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (VoidedEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

}
