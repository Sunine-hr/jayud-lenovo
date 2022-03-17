package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 货架移动任务:订单来源(1系统创建 2人工创建)
 */
@Getter
@AllArgsConstructor
public enum ShelfMoveTaskOrderSourceEnum {


    ONE(1, "系统创建"),
    TWO(2, "人工创建"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (ShelfMoveTaskOrderSourceEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ShelfMoveTaskOrderSourceEnum getEnum(Integer code) {
        for (ShelfMoveTaskOrderSourceEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (ShelfMoveTaskOrderSourceEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

}
