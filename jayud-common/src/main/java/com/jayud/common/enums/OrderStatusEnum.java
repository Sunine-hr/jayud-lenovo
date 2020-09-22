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
    CUSTOMS_C_0("C_0","未接单"),
    CUSTOMS_C_1("C_1","报关接单"),
    CUSTOMS_C_2("C_2","报关打单"),
    CUSTOMS_C_3("C_3","报关复核"),
    CUSTOMS_C_3_1("C_3_1","报关复核驳回"),
    CUSTOMS_C_4("C_4","报关申报"),
    CUSTOMS_C_5("C_5","报关放行"),
    CUSTOMS_C_5_1("C_5_1","报关放行驳回"),
    CUSTOMS_C_6("C_6","报关异常"),


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
