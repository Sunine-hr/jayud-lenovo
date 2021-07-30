package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SignEnum {

    ONE(0,"未标记"),
    TWO(1,"已标记"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (SignEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
