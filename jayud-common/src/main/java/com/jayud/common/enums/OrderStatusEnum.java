package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    //业务单类型
    CBG("CBG","纯报关"),
    ZGYS("ZGYS","中港运输"),
    SZZZC("SZZZC","深圳中转仓"),
    CKBG("CKBG","出口报关"),
    XGQG("XGQG","香港清关"),

    //主订单状态
    MAIN_1("1","正常"),
    MAIN_2("2","草稿"),
    MAIN_3("3","关闭"),
    MAIN_4("4","待补全"),

    //主干流程节点
    MAIN_PROCESS_1("M_1","已下单"),
    MAIN_PROCESS_2("M_2","运输中"),
    MAIN_PROCESS_3("M_3","报关中"),
    MAIN_PROCESS_4("M_4","已完成"),

    //纯报关子订单流程节点+纯报关子订单状态
    CUSTOMS_C_0("C_0","待接单"), //仅报关子订单状态用
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

    //中港子订单流程节点+中港子订单状态
    TMS_T_0("T_0","待接单"), //仅中港子订单状态用
    TMS_T_1("T_1","运输接单"),
    TMS_T_2("T_2","运输派车"),
    TMS_T_3("T_3","运输审核"),
    TMS_T_3_1("T_3_1","运输审核不通过"),//仅中港子订单状态用
    TMS_T_4("T_4","确认派车"),
    TMS_T_5("T_5","车辆提货"),
    TMS_T_6("T_6","车辆过磅"),
    TMS_T_7("T_7","通关前审核"),//通关前审核必须先操作外部报关放行
    TMS_T_7_1("T_7_1","通关前审核不通过"),//仅中港子订单状态用
    TMS_T_8("T_8","通关前复核"),
    TMS_T_8_1("T_8_1","通关前复核不通过"),//仅中港子订单状态用
    TMS_T_9("T_9","车辆通关"),
    TMS_T_9_1("T_9_1","车辆通关查验"),//仅中港子订单状态用
    TMS_T_9_2("T_9_2","车辆通关其他异常"),//仅中港子订单状态用
    TMS_T_10("T_10","车辆入仓"),
    TMS_T_11("T_11","中转仓卸货"),//反馈状态里面确认
    TMS_T_12("T_12","中转仓装货"),//反馈状态里面确认
    TMS_T_13("T_13","车辆出仓"),
    TMS_T_14("T_14","车辆派送"),
    TMS_T_15("T_15","确认签收"),

    //外部报关放行
    EXT_CUSTOMS_RELEASE("E_C_0","外部报关放行"),

    //香港清关
    HK_CLEAR_0("HK_C_0","无缝申请"),
    HK_CLEAR_1("HK_C_1","香港清关"),

    //内陆运输 TODO

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
