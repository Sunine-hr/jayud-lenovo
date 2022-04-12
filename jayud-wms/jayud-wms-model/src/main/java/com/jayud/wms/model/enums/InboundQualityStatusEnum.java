package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author ciro
 * @date 2022/4/11 13:34
 * @description: 入库-质检状态枚举
 */
@Getter
@AllArgsConstructor
public enum InboundQualityStatusEnum {

    UNFINISH(1,"未质检"),
    FINISH(2,"已质检");

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (InboundQualityStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

}
