package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 订单标识
 */
@Getter
@AllArgsConstructor
public enum SubOrderSignEnum {

    KY("ky", "air_order", "空运"),
    BG("bg", "order_customs", "报关"),
    ZGYS("zgys", "order_transport", "中港运输");
    //    private Integer code;
    private String signOne;
    private String signTwo;
    private String desc;

    public static String getSignOne2SignTwo(String signOne) {
        for (SubOrderSignEnum value : values()) {
            if (Objects.equals(signOne, value.getSignOne())) {
                return value.getSignTwo();
            }
        }
        return "";
    }

//    public static Integer getCode(String desc) {
//        for (SubOrderSignEnum value : values()) {
//            if (Objects.equals(desc, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return -1;
//    }
}
