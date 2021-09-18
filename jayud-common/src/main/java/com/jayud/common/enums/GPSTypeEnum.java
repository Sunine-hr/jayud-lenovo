package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * GPS类型
 */
@Getter
@AllArgsConstructor
public enum GPSTypeEnum {

    ONE(1, "云港通"),
    TWO(2, "北斗"),
    FOUND_NOT(-1, "暂不支持"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (GPSTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static GPSTypeEnum getEnum(Integer code) {
        for (GPSTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return FOUND_NOT;
    }

    public static Integer getCode(String desc) {
        for (GPSTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }


    /**
     * main|zgys|bg|ky
     * @param cmd
     * @return
     */
//    public static Integer getCode(String cmd) {
//        for (BusinessTypeEnum value : values()) {
//            if (Objects.equals(cmd, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return -1;
//    }
}
