package com.jayud.oauth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SystemUserStatusEnum {

    ON(1,"On"),
    OFF(0,"Off"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (SystemUserStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
