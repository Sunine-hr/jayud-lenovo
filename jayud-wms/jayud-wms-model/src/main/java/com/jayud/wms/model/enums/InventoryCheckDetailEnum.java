package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 库存盘点明细表
 */
@Getter
@AllArgsConstructor
public enum InventoryCheckDetailEnum {

    ONE(1, "未盘点"),
    TWO(2, "已盘点"),
    THREE(3, "已过账"),

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
        for (InventoryCheckDetailEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static InventoryCheckDetailEnum getEnum(Integer code) {
        for (InventoryCheckDetailEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (InventoryCheckDetailEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
