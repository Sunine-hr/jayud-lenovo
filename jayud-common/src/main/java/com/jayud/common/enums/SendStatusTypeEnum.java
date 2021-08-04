package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 账单类型
 */
@Getter
@AllArgsConstructor
public enum SendStatusTypeEnum {

    SUCCESS(1, "发送成功"),
    FAIL(2, "发送失败"),
    WAIT(3, "等待发送"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (SendStatusTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static SendStatusTypeEnum getEnum(Integer code) {
        for (SendStatusTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (SendStatusTypeEnum value : values()) {
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
