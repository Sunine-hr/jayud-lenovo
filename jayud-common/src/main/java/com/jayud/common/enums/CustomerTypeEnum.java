package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 业务类型(空运,中港运输,纯报关,...)
 */
@Getter
@AllArgsConstructor
public enum CustomerTypeEnum {

    ONE(1, "同行"),
    TWO(2, "电商"),
    THREE(3, "货代"), FOUR(4, "供应商"),
    FIVE(5, "工厂"), SIX(6, "贸易"),
    SEVEN(7, "国际");
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (CustomerTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (CustomerTypeEnum value : values()) {
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
