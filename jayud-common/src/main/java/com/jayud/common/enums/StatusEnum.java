package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 业务类型(空运,中港运输,纯报关)
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    DISABLE(0, "禁用"),ENABLE(1,"启用"),DELETE(2,"删除");
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (StatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (StatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
