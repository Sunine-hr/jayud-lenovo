package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 通知单状态
 */
@Getter
@AllArgsConstructor
public enum ReceiptNoticeStatusEnum {

    CREATE(1, "创建"),
    CANCEL(2, "上游取消"),
    RECEIVING(3, "转收货"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (ReceiptNoticeStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ReceiptNoticeStatusEnum getEnum(Integer code) {
        for (ReceiptNoticeStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (ReceiptNoticeStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
