package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderAddressEnum {
    //地址类型
    DELIVER_GOODS(0, "发货"),
    RECEIVING_GOODS(1, "收货"),
    NOTICE(2, "通知");

    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (OrderAddressEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (OrderAddressEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return 0;
    }
}
