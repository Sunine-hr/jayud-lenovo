package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 通知单状态
 */
@Getter
@AllArgsConstructor
public enum MaterialSnStatusEnum {

    ONE(1, "未收货"),
    TWO(2, "收货中"),
    THREE(3, "已收货"),
    FOUR(4, "撤销收货"),
    ;
    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    public static String getDesc(String code) {
        for (MaterialSnStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static MaterialSnStatusEnum getEnum(Integer code) {
        for (MaterialSnStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (MaterialSnStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
