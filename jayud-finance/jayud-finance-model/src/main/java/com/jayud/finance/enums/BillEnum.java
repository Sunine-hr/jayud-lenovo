package com.jayud.finance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum BillEnum {

    //账单状态
    B_1("B_1","生成账单"),



    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (BillEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
