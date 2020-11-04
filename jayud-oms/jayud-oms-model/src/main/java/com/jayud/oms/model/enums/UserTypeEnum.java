package com.jayud.oms.model.enums;

public enum UserTypeEnum {

    user("1", "用户"),
    customer("2", "客户"),
    supplier("3", "供应商");

    private String code;
    private String desc;

    UserTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
