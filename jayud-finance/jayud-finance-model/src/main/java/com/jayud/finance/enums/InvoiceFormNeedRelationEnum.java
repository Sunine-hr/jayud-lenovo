package com.jayud.finance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoiceFormNeedRelationEnum {
    COMPANY("CustomsFinanceCoRelation"),
    FEE("CustomsFinanceFeeRelation"),
    NONE("none")
    ;

    private String relationTable;
}
