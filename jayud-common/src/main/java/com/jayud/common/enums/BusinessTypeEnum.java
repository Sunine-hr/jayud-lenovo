package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 业务类型(空运,中港运输,纯报关)
 */
@Getter
@AllArgsConstructor
public enum BusinessTypeEnum {

    KY(0, "空运"),;;
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (BusinessTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (BusinessTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
