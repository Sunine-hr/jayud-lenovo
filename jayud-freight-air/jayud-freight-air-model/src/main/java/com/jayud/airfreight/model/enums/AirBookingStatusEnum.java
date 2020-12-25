package com.jayud.airfreight.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 贸易方式
 */
@Getter
@AllArgsConstructor
public enum AirBookingStatusEnum {
    CONFIRM(0, "确认"),
    PENDING_CONFIRMED(1, "待确认"),
    DELETE(2, "删除"),;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (AirBookingStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }


}
