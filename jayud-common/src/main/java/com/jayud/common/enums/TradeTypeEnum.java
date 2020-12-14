package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 贸易类型
 */
@Getter
@AllArgsConstructor
public enum TradeTypeEnum {

    KY(1, "进口"), CBG(2, "出口");
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (TradeTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (TradeTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
