package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 应收/应付订单类型
 */
@Getter
@AllArgsConstructor
public enum ReceivableAndPayableOrderTypeEnum {
    MAIN("main", "主订单"), KY("ky", "空运")
    , CBG("bg", "纯报关"), ZGYS("zgys", "中港运输"),
    OTHER("other", "其他");
    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (ReceivableAndPayableOrderTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static String getCode(String desc) {
        for (ReceivableAndPayableOrderTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return null;
    }

    public static ReceivableAndPayableOrderTypeEnum getEnum(String code) {
        for (ReceivableAndPayableOrderTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return OTHER;
    }
}
