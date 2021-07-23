package com.jayud.oms.model.enums;


import java.util.Objects;

public enum ErrorEnum {
    ONE(-1, "未输入参数"),
    TWO(-2, "参数类型或结构错误"),
    Three(-3,"令牌错误"),
    FOUR(-4,"车牌号列表错误"),
    SIX(-6,"无车牌权限"),
    SEVEN(-7,"超出每分钟配额"),
    EIGHT(-8,"超出配额"),
    NINE(-9,"系统错误"),
    TEN(-10,"未输入车牌"),
    ELEVEN(-11,"无车牌权限"),
    FOUR_ZERO_FOUR(404,"其它参数错误或无数据")
    ;

    private Integer code;
    private String desc;


    public static String getDesc(Integer code) {
        for (ErrorEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    ErrorEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
