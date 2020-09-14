package com.jayud.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    EMPLOYEE_TYPE("1","员工类型"),
    CUSTOMER_TYPE("2","客户类型"),
    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (UserTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
