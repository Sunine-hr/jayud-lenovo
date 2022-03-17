package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 货架位置状态
 */
@Getter
@AllArgsConstructor
public enum ShelfLocationStatusEnum {


    ONE(1, "在固定位"),
    TWO(2, "在移动中"),
    THREE(3, "在工作台")
    ;
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (ShelfLocationStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ShelfLocationStatusEnum getEnum(Integer code) {
        for (ShelfLocationStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (ShelfLocationStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

}
