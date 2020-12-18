package com.jayud.airfreight.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 贸易方式
 */
@Getter
@AllArgsConstructor
public enum VivoRejectionStatusEnum {
    PENDING_SUBMITTED(1, "待提交"),
    PENDING_CONFIRMED(2, "待确认"),
    CONFIRM(3, "已确认"),;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (VivoRejectionStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }


}
