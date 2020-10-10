package com.jayud.finance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvoiceTypeEnum {
    RECEIVABLE("receivable"),
    PAYABLE("payable"),
    ALL("all"),
    ;
    private String type;
}
