package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 通知单状态
 */
@Getter
@AllArgsConstructor
public enum SowingResultsStatusEnum {

    ONE(1, "未更换"),
    TWO(2, "已更换"),
    THREE(3, "确认上架"),
    FOUR(4, "完成上架"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (SowingResultsStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static SowingResultsStatusEnum getEnum(Integer code) {
        for (SowingResultsStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (SowingResultsStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
