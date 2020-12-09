package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 业务类型(空运,中港运输,纯报关)
 */
@Getter
@AllArgsConstructor
public enum AirProcessStatusEnum {

    PROCESSING(0, "进行中"),COMPLETE(1,"完成"),DRAFT(2,"草稿");
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (AirProcessStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (AirProcessStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
