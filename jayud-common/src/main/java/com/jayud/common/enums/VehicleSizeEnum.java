package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 车辆尺寸
 */
@Getter
@AllArgsConstructor
public enum VehicleSizeEnum {

    SIZE_3T(1, "3T"),
    SIZE_5T(2, "5T"),
    SIZE_8T(3, "8T"),
    SIZE_10T(4, "10T"),
    SIZE_12T(5, "12T"),
    SIZE_20GP(6, "20GP"),
    SIZE_40GP(7, "40GP"),
    SIZE_45GP(8, "45GP"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (VehicleSizeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
