package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 通知单状态
 */
@Getter
@AllArgsConstructor
public enum ShelfOrderTaskStatusEnum {

    ONE(1, "待上架"),
    TWO(2, "上架中"),
    THREE(3, "已上架"),
    FOUR(4, "强制上架"),
    FIVE(5, "撤销上架");

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (ShelfOrderTaskStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ShelfOrderTaskStatusEnum getEnum(Integer code) {
        for (ShelfOrderTaskStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (ShelfOrderTaskStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
