package com.jayud.oceanship.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 贸易方式
 */
@Getter
@AllArgsConstructor
public enum SeaBookShipStatusEnum {
    CONFIRM(0, "确认"),
    PENDING_CONFIRMED(1, "待确认"),
    DELETE(2, "删除"),;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (SeaBookShipStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }


}
