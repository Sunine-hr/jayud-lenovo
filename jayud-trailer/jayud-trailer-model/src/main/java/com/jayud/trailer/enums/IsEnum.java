package com.jayud.trailer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum IsEnum {
    YES(true, "是"),
    NO(false, "否");

    private boolean code;
    private String desc;

    public static String getDesc(boolean code) {
        for (IsEnum value : values()) {
            if (Objects.equals(code, value.isCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
