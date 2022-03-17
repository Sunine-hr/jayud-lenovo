package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 任务状态代码(1 待移库，2 已移库)
 */
@Getter
@AllArgsConstructor
public enum TaskStatusCodeEnum {


    ONE(1, "待移库"),
    TWO(2, "已移库"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (TaskStatusCodeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static TaskStatusCodeEnum getEnum(Integer code) {
        for (TaskStatusCodeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (TaskStatusCodeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }

}
