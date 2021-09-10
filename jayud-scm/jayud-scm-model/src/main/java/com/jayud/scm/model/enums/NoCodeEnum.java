package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


/**
 * no_rule
 */
@Getter
@AllArgsConstructor
public enum NoCodeEnum {


    COMMODITY("1001","商品编号"),
    D001("D001","委托单号"),
    CUSTOMER("1002","客户编号"),
    FEE_MODEL("1003","结算设置编号"),
    HUB_SHIPPING("1004","出库单号"),
    CHECK_ORDER("1005","提验货单号"),
    HUB_RECEIVING("1006","入库单号"),
    HG_TRUCK("1007","车次编号"),
    HG_BILL("1007","报关编号"),
    ACCOUNT_BANK_BILL("1009","水单编号"),
    ACCT_RECEIPT("1010","收款编号"),
    ACCT_PAY("1011","付款编号"),
    VERIFICATION_REOCRDS("1012","核销单号"),
    EXPORT_TAX_INVOICE("1013","进项票单号"),
    ;

    private String code;
    private String desc;

    public static String getDesc(Integer code) {
        for (NoCodeEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
