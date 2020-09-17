package com.jayud.oauth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    //共有如下几个审核流程
    //1.集团管理:财务新增法人主体-总经办审核法人主体
    //2.集团管理:新增账号管理-总经办人员审核

    WAIT_AUDIT(1,"待审核"),
    AUDIT_SUCCESS(2,"审核通过"),
    AUDIT_FAIL(0,"审核拒绝"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (StatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
