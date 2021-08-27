package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum TrainNumberStatusEnum {


    ZERO(0,"未装车"),
    ONE(1,"已发车"),
    TWO(2,"已装车"),
    THREE(3,"已过关"),
    FOUR(4,"已到货"),
    FIVE(5,"直送客户")
    ;

    private Integer code;
    private String desc;

    public static String getDesc(Integer code) {
        for (TrainNumberStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
