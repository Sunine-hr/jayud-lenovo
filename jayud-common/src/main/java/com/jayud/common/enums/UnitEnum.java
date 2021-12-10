package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 业务类型(空运,中港运输,纯报关,...)
 */
@Getter
@AllArgsConstructor
public enum UnitEnum {

    KGS("KGS","KGS"),CBM("CBM","CBM"),CTNS("CTNS","CTNS")
    ,PCS("PCS","PCS"),BILL("BILL","BILL"),Pallet("Pallet","Pallet");
    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (UnitEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return null;
    }

    public static String getCode(String desc) {
        for (UnitEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return null;
    }



}
