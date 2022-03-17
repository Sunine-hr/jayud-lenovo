package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 入库流程状态
 */
@Getter
@AllArgsConstructor
public enum InboundOrderProStatusEnum {

    ONE(1, "收货通知单"),
    TWO(2, "收货单"),
    THREE(3, "质检"),
    FOUR(4, "入库播种"),
    FIVE(5, "上架订单"),
    ;
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (InboundOrderProStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static InboundOrderProStatusEnum getEnum(Integer code) {
        for (InboundOrderProStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (InboundOrderProStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }
}
