package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 通知单状态
 */
@Getter
@AllArgsConstructor
public enum MaterialStatusEnum {

    ONE(1, "未收货"),
    TWO(2, "收货中"),
    THREE(3, "已收货"),
    FOUR(4, "撤销收货"),
    FIVE(5, "待上架"),
    six(6, "已上架"),
    ;
    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    public static String getDesc(Integer code) {
        for (MaterialStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static MaterialStatusEnum getEnum(Integer code) {
        for (MaterialStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (MaterialStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
