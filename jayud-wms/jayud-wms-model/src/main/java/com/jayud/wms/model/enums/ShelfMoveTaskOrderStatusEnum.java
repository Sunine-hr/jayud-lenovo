package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 货架移动任务:订单状态(1待移动 2移动中 3已完成)
 */
@Getter
@AllArgsConstructor
public enum ShelfMoveTaskOrderStatusEnum {


    ONE(1, "待移动"),
    TWO(2, "移动中"),
    THREE(3, "已完成"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (ShelfMoveTaskOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ShelfMoveTaskOrderStatusEnum getEnum(Integer code) {
        for (ShelfMoveTaskOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (ShelfMoveTaskOrderStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }


}
