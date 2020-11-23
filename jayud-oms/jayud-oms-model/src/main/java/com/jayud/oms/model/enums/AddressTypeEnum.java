package com.jayud.oms.model.enums;

import java.util.Objects;

public enum AddressTypeEnum {
    ZERO("0", "提货地址"),
    ONE("1", "送货地址");

    private String code;
    private String desc;

    AddressTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (AddressTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
