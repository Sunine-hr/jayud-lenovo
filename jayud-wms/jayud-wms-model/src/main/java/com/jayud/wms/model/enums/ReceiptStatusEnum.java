package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 通知单状态
 */
@Getter
@AllArgsConstructor
public enum ReceiptStatusEnum {

    ONE(1, "待收货"),
    TWO(2, "部分收货"),
    THREE(3, "完全收货"),
    FOUR(4, "整单撤销"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (ReceiptStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ReceiptStatusEnum getEnum(Integer code) {
        for (ReceiptStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (ReceiptStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
