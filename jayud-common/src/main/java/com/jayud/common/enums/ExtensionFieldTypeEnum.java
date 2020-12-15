package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 扩展字段类型
 */
@Getter
@AllArgsConstructor
public enum ExtensionFieldTypeEnum {

    VIVO(0, "vivo"),;
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (ExtensionFieldTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (ExtensionFieldTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
