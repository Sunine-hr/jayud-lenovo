package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 移库类型代码
 * 移库类型代码(1 物料移库，2 容器移库，3 库位移库)
 */
@Getter
@AllArgsConstructor
public enum MovementTypeCodeEnum {

    ONE(1, "物料移库"),
    TWO(2, "容器移库"),
    THREE(3, "库位移库")
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (MovementTypeCodeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static MovementTypeCodeEnum getEnum(Integer code) {
        for (MovementTypeCodeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (MovementTypeCodeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

}
