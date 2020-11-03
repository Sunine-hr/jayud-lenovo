package com.jayud.oms.model.enums;

/**
 * 合同类型
 */
public enum ContractTypeEnum {

    CUSTOMER("0","客户合同"),
    SUPPLIER("1,","供应商合同");
    private String code;
    private String desc;

    ContractTypeEnum(String code, String desc) {
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
