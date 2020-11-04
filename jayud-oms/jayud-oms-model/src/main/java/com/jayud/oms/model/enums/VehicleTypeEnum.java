package com.jayud.oms.model.enums;

import java.util.Objects;

/**
 * 车辆类型枚举
 */
public enum VehicleTypeEnum {

    TON_CAR(1, "吨车"),
    CABINET_CAR(2, "柜车");

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

    VehicleTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
