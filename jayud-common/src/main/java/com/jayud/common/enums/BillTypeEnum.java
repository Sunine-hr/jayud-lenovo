package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 账单类型
 */
@Getter
@AllArgsConstructor
public enum BillTypeEnum {

    RECEIVABLE(0, "应收"),
    PAYMENT(1, "应付"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (BillTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static BillTypeEnum getEnum(Integer code) {
        for (BillTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (BillTypeEnum value : values()) {
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
