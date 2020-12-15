package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * VIVO接口描述
 */
@Getter
@AllArgsConstructor
public enum VivoInterfaceDescEnum {

    ONE(1, "vivo抛订舱数据到货代"),
    TWO(2, "货代确认订舱信息"), THREE(3, "vivo取消订舱单"),
    FOUR(4, "货代获取订舱文件"),FIVE(5, "订舱驳回接口"),
    SIX(6, "vivo抛派车信息到货代"),SEVEN(7, "货代抛转车辆信息"),
    EIGHT(8, "vivo取消派车"),NINE(9, "派车驳回接口"),
    TEN(10, "货代抛转空运跟踪信息"),ELEVEN(11, "货代抛转空运提单文件"),
    TWELVE(12, "货代抛空运费用数据到vivo"),;
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (VivoInterfaceDescEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (VivoInterfaceDescEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
