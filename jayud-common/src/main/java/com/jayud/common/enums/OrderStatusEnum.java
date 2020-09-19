package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    //主订单
    MAIN_1("1","正常"),
    MAIN_2("2","草稿"),
    MAIN_3("3","关闭"),

    //纯报关子订单
    CUSTOMS_0("0","未接单"),
    CUSTOMS_1("1","已接单"),
    CUSTOMS_2("2","接单中"),
    CUSTOMS_3("3","放行通过"),
    CUSTOMS_4("4","放行驳回"),
    CUSTOMS_5("4","已完成"),

    //费用状态
    COST_0("0","审核驳回"),
    COST_1("1","草稿"),
    COST_2("2","提交审核"),
    COST_3("3","审核通过")

     ;

    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (OrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
