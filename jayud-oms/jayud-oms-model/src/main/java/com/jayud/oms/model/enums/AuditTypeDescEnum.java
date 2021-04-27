package com.jayud.oms.model.enums;

/**
 * 审核描述
 */
public enum AuditTypeDescEnum {
    ONE("1", "supplier_info表", "供应商审核"),
    TWO("2", "customs_questionnaire表", "海关调查问卷审核"),
    THREE("3", "business_dev_evaluation", "商业伙伴开发评估审核"),;

    private String code;
    private String table;
    private String desc;

    AuditTypeDescEnum(String code, String table, String desc) {
        this.code = code;
        this.table = table;
        this.desc = desc;
    }


    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
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
