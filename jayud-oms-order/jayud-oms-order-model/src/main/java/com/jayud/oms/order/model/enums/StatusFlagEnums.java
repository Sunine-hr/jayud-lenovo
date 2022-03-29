package com.jayud.oms.order.model.enums;

import com.jayud.common.enums.BillTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 状态类型
 */
@Getter
@AllArgsConstructor
public enum StatusFlagEnums {
    PRE_SUBMIT(1,"暂存"),
    SUBMIT(2,"正常"),
    CLOSE(3,"关闭"),
    ;

    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (StatusFlagEnums value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static StatusFlagEnums getEnum(Integer code) {
        for (StatusFlagEnums value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (StatusFlagEnums value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
