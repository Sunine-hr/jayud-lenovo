package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 业务类型(空运,中港运输,纯报关,...)
 */
@Getter
@AllArgsConstructor
public enum BusinessTypeEnum {

    KY(0, "空运"),
    CBG(1,"纯报关"),
    ZGYS(2,"中港运输"),
    BG(3,"报关"),HY(4,"海运"),
    NL(5,"内陆运输"),TC(6,"拖车"),
    CK(7,"入库"),RK(8,"出库");
    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (BusinessTypeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static Integer getCode(String desc) {
        for (BusinessTypeEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return -1;
    }


    /**
     * main|zgys|bg|ky
     * @param cmd
     * @return
     */
//    public static Integer getCode(String cmd) {
//        for (BusinessTypeEnum value : values()) {
//            if (Objects.equals(cmd, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return -1;
//    }
}
