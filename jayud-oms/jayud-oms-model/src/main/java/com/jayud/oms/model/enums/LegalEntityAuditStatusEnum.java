package com.jayud.oms.model.enums;

import java.util.Objects;

/**
 * 审核状态
 */
public enum LegalEntityAuditStatusEnum {
    FAIL("0", "拒绝"),
    PENDING_AUDIT("1", "待审核"),
    SUCCESS("2", "通过"),
    ;

    private String code;
    private String desc;

    LegalEntityAuditStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (LegalEntityAuditStatusEnum value : values()) {
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
