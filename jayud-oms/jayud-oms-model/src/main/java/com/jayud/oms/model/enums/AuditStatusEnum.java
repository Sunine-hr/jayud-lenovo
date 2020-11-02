package com.jayud.oms.model.enums;

import java.util.Objects;

/**
 * 审核状态
 */
public enum AuditStatusEnum {
    KF_WAIT("0", "待客服审核"),
    CW_WAIT("1", "待财务审核"),
    ZJB_WAIT("2", "待总经办审核"),
    SUCCESS("10", "审核通过"),
    FAIL("11", "审核拒绝");

    private String code;
    private String desc;

    AuditStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (AuditStatusEnum value : values()) {
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
