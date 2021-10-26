package com.jayud.tms.model.enums;

import java.util.Objects;

/**
 * 供应链订单状态枚举
 */
public enum ScmOrderStatusEnum {
    ASSEMBLY_VEHICLE("已装车", "已装车"),
    DEPART_VEHICLE("已发车", "已发车"),
    THROUGH_CUSTOMS("已过关", "已过关"),
    ARRIVED("已到货", "已到货");

    private String code;
    private String desc;

    ScmOrderStatusEnum(String code, String desc) {
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
        for (ScmOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
