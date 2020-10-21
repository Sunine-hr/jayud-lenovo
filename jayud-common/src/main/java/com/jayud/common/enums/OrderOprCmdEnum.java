package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderOprCmdEnum {

    CONFIRM_ORDER("confirmOrder","报关接单"),
    AUDIT_FAIL("auditFail","审核不通过"),
    ORDER_LIST("orderList","订单列表"),
    ISSUE_ORDER("issueOrder","报关打单"),
    TO_CHECK("declare","报关复核"),
    DECLARE("toCheck","报关申报"),
    RELEASE_CONFIRM("releaseConfirm","放行确认"),
    AUDIT_FAIL_EDIT("auditFailEdit","审核不通过的编辑"),
    GO_CUSTOMS_SUCCESS("goCustomsSuccess","通关完成"),
    CUSTOMS_CHECK("customsCheck","通关查验"),
    CUSTOMS_EXCEP("customsExcep","通关其他异常"),

     ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (OrderOprCmdEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
