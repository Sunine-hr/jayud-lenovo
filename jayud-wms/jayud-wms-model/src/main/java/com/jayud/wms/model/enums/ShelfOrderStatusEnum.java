package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 通知单状态
 */
@Getter
@AllArgsConstructor
public enum ShelfOrderStatusEnum {

    ONE(1, "待上架"),
    TWO(2, "已上架"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (ShelfOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ShelfOrderStatusEnum getEnum(Integer code) {
        for (ShelfOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (ShelfOrderStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
