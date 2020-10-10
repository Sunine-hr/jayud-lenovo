package com.jayud.finance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表单的类型，是主应收应付还是其他应收应付
 */
@Getter
@AllArgsConstructor
public enum InvoiceFormTypeEnum {
    MAIN("main"),
    OTHER("other"),
    BOTH("both")
    ;

    private String type;

}
