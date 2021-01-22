package com.jayud.tms.model.enums;

import java.util.Objects;

/**
 * 中港运输地址类型枚举
 */
public enum OrderTakeAdrTypeEnum {
    ONE(1, "提货地址"),
    TWO(2, "送货地址");

    private Integer code;
    private String desc;

    OrderTakeAdrTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (OrderTakeAdrTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
