package com.jayud.oms.model.enums;

import java.util.Objects;

/**
 * 结算类型
 */
public enum SettlementTypeEnum {
    TICKET("0","票结"),
    WEEK("1","周结"),
    MONTH("2","月结"),
    TICKET_AFTER("3","票后"),
    PRE_CHARGE("4","预充值");

    private String code;
    private String desc;

    SettlementTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (SettlementTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
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
}
