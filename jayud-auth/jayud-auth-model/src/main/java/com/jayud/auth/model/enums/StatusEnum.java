package com.jayud.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    INVALID("0", "无效"),
    ENABLE("1", "有效"),
    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (StatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
