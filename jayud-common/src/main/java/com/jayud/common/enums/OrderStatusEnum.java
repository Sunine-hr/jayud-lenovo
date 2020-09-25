package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    //主订单状态
    MAIN_1("1","正常"),
    MAIN_2("2","草稿"),
    MAIN_3("3","关闭"),

    //主干流程节点
    MAIN_PROCESS_1("M_1","已下单"),
    MAIN_PROCESS_2("M_2","运输中"),
    MAIN_PROCESS_3("M_3","报关中"),
    MAIN_PROCESS_4("M_4","已完成"),

    //纯报关子订单流程节点+纯报关子订单状态
    CUSTOMS_C_0("C_0","未接单"), //仅报关子订单状态用
    CUSTOMS_C_1("C_1","报关接单"),
    CUSTOMS_C_2("C_2","报关打单"),
    CUSTOMS_C_3("C_3","报关复核"),
    CUSTOMS_C_4("C_4","报关申报"),
    CUSTOMS_C_5("C_5","报关放行"),
    CUSTOMS_C_5_1("C_5_1","报关放行驳回"),//仅报关子订单状态用
    CUSTOMS_C_6("C_6","通关确认"),
    CUSTOMS_C_6_1("C_6_1","通关查验"),//仅报关子订单状态用
    CUSTOMS_C_6_2("C_6_2","通关其他异常"),//仅报关子订单状态用
    CUSTOMS_C_7("C_7","录入费用"),
    CUSTOMS_C_8("C_8","费用审核"),
    CUSTOMS_C_Y("C_Y","报关异常"),//仅报关子订单状态用


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
