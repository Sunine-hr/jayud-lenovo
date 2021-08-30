package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 账单类型
 */
@Getter
@AllArgsConstructor
public enum CustomsBizModelEnum {
    //业务模式(1-陆路运输 2-空运 3-海运 4-快递 5-内陆)
    ONE(1, "陆路运输"),
    TWO(2, "空运"),
    THREE(3, "海运"),
    FOUR(4, "快递"),
    FIVE(5, "内陆"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (CustomsBizModelEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static CustomsBizModelEnum getEnum(Integer code) {
        for (CustomsBizModelEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (CustomsBizModelEnum value : values()) {
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
