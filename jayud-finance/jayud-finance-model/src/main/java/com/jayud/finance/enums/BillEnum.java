package com.jayud.finance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum BillEnum {

    //账单状态
    B_1("B_1","生成对账单"),//客服
    B_2("B_2","审核对账单"),//客服主管
    B_2_1("B_2_1","审核对账单不通过"),//客服主管
    B_3("B_3","提交财务"),//客服提交财务
    B_4("B_4","审核对账单"),//财务
    B_4_1("B_4_1","审核对账单不通过"),//财务
    B_5("B_5","开票申请"),//客服
    B_6("B_6","开票申请审核"),//财务
    B_6_1("B_6_1","开票申请审核不通过"),//财务
    B_7("B_1","推金碟"),//财务


    //付款申请状态
    F_0("0","未申请"),
    F_1("1","待审核"),
    F_2("2","审核通过"),
    F_3("3","审核驳回"),
    F_4("4","付款申请作废"),




    ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (BillEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
