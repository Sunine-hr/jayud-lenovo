package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 账单类型
 */
@Getter
@AllArgsConstructor
public enum MsgPushInstructionEnum {

    CMD1("order", "订单"),
    ;
    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (MsgPushInstructionEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static MsgPushInstructionEnum getEnum(String code) {
        for (MsgPushInstructionEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static String getCode(String desc) {
        for (MsgPushInstructionEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
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
