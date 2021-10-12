package com.jayud.oms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum EmploymentFeeStatusEnum {

    SUBMIT("0", "待提交"),
    SUBMITTED("1", "已提交"),
    DRAFT("2", "草稿"),
    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (EmploymentFeeStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
