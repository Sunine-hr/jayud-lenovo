package com.jayud.oms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 司机接单状态
 */
@Getter
@AllArgsConstructor
public enum DriverOrderStatusEnum {

    ALL("0", "全部"),
    PENDING("1", "待提货"),
    IN_TRANSIT("2", "运输中"),
    FINISHED("3", "已完结"),
    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (DriverOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static DriverOrderStatusEnum getEnumObj(String code) {
        for (DriverOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }
}
