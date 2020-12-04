package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 属于哪个平台创建的记录
 */
@Getter
@AllArgsConstructor
public enum CreateUserTypeEnum {

    LOCAL(0, "本系统"), VIVO(1, "vivo");
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (CreateUserTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (CreateUserTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
