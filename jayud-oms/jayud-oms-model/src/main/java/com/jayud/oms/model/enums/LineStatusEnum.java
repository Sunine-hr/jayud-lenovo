package com.jayud.oms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 线路审核状态
 */
@Getter
@AllArgsConstructor
public enum LineStatusEnum {
    // 审核状态(1-待审核 2-审核通过 3-终止 0-拒绝)
    WAIT_AUDIT(1,"待审核"),
    AUDIT_SUCCESS(2,"审核通过"),
    AUDIT_FAIL(0,"拒绝"),
    STOP(3,"终止")
    ;

    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (LineStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
