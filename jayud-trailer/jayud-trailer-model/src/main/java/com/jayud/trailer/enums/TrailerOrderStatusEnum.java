package com.jayud.trailer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 派车状态
 */
@Getter
@AllArgsConstructor
public enum TrailerOrderStatusEnum {
    CONFIRM(0, "确认"),
    PENDING_CONFIRMED(1, "待确认"),
    DELETE(2, "删除");

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (TrailerOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }


}
