package com.jayud.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 报价数据类型
 */
@Getter
@AllArgsConstructor
public enum QuotationDataTypeEnum {
    TEMPLATE(1, "报价模板"),
    MANAGEMENT(2, "报价管理");

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (QuotationDataTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (QuotationDataTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
