package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 库存移动订单表 状态
 */
@Getter
@AllArgsConstructor
public enum InventoryMovementEnum {

    ONE(1, "待移库"),
    TWO(2, "已移库"),
    THREE(3, "待定"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (InventoryMovementEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static InventoryMovementEnum getEnum(Integer code) {
        for (InventoryMovementEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (InventoryMovementEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
