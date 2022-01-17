package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum StatusEnum {


    WAIT_AUDIT(1,"待审核"),
    AUDIT_SUCCESS(2,"审核通过"),
    AUDIT_FAIL(0,"审核不通过"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (StatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
