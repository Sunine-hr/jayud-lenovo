package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 车型类型
 */
@Getter
@AllArgsConstructor
public enum VehicleTypeEnum {
    //(1-3T 2-5t 3-8T 4-10T)
    ONE(1, "3T"), TWO(2, "5T"), THREE(3, "8T"),
    FOUR(4, "10T");
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (VehicleTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (VehicleTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return null;
    }
}
