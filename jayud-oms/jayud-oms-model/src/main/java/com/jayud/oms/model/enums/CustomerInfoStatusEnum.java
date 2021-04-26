package com.jayud.oms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CustomerInfoStatusEnum {

    //审核状态( 0-待客服审核 1-待财务审核 2-待总经办审核 10-通过 11-拒绝)
    KF_WAIT_AUDIT("0","待客服审核"),
    CW_WAIT_AUDIT("1","待财务审核"),
    ZJB_WAIT_AUDIT("2","待总经办审核"),
    DRAFT("3","草稿"),
    AUDIT_SUCCESS("10","审核通过"),
    AUDIT_FAIL("11","审核拒绝")
    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (CustomerInfoStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
