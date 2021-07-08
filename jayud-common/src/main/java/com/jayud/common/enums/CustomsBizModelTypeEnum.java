package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 报关业务类型
 */
@Getter
@AllArgsConstructor
public enum CustomsBizModelTypeEnum {
    ZG(1, "陆路运输"),
    KY(2, "空运"),
    HY(3, "海运"),
    KD(4, "快递"),
    NL(5, "内陆"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (CustomsBizModelTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (CustomsBizModelTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

    public static CustomsBizModelTypeEnum getEnum(Integer code) {
        for (CustomsBizModelTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
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
