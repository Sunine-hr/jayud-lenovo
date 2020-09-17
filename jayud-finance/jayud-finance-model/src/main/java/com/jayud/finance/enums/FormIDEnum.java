package com.jayud.finance.enums;

import lombok.Getter;

@Getter
public enum FormIDEnum {

    MATERIAL("BD_MATERIAL"),//物料
    OUT_STOCK("SAL_OUTSTOCK"),//出库单
    STOCK("BD_STOCK"),//仓库
    CUSTOMER("BD_Customer"),//客户
    PAYABLE("AP_Payable"),//应付单PAYABLE
    RECEIVABLE("AR_receivable"),//应收单
    ORGANIZATION("ORG_Organizations"),//组织架构
    DEPARTMENT("BD_Department"),//部门
    EXPENSE("BD_Expense"),//费用
    SUPPLIER("BD_Supplier"),//供应商
    ASSISTANT("BOS_ASSISTANTDATA_DETAIL"),//数据类型（辅助属性值）
    CURRENCY("BD_Currency"),//币别
    ;


    private String formid;

    FormIDEnum(String formid) {
        this.formid = formid;
    }

}
