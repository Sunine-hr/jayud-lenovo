package com.jayud.airfreight.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 贸易方式
 */
@Getter
@AllArgsConstructor
public enum AirOrderOptUserEnum {
    VIVO(0, "vivo");

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (AirOrderOptUserEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (AirOrderOptUserEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return 0;
    }
}
