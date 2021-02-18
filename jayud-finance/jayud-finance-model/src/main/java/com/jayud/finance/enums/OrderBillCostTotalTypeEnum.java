package com.jayud.finance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 业务类型(空运,中港运输,纯报关,...)
 */
@Getter
@AllArgsConstructor
public enum OrderBillCostTotalTypeEnum {

    PAYMENT("1", "应付"), RECEIVABLE("2","应收");
    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (OrderBillCostTotalTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static String getCode(String desc) {
        for (OrderBillCostTotalTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return "";
    }


}
