package com.jayud.airfreight.model.enums;

import com.jayud.common.enums.OrderAddressEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 贸易方式
 */
@Getter
@AllArgsConstructor
public enum AirOrderTermsEnum {
    CIF(0, "CIF"),
    DUU(1, "DUU"),
    FOB(2, "FOB"),
    DDP(3, "DDP");

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (AirOrderTermsEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (AirOrderTermsEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return 0;
    }
}
