package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 流程状态
 */
@Getter
@AllArgsConstructor
public enum ProcessStatusEnum {

    PROCESSING(0, "进行中"), COMPLETE(1, "完成"), DRAFT(2, "草稿"), CLOSE(3, "关闭");
    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (ProcessStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (ProcessStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
