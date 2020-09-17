package com.jayud.oms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum RoleKeyEnum {

    BUSINESS_MANAGER("business_manager","业务员"),
    CUSTOMER_SERVICE("customer_service","客服")
    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (RoleKeyEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
