package com.jayud.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ActionEnum {


    ZERO(0,"无权限"),
    ONE(1,"个人"),
    TWO(2,"团体"),
    THREE(3,"所有"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (ActionEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
