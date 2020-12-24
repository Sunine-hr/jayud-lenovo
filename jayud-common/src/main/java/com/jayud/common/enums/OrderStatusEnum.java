package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    //业务单类型
    CBG("CBG", "纯报关"),
    KY("KY", "空运"),
    ZGYS("ZGYS", "中港运输"),
    NLYS("NLYS", "内陆运输"),
    SZZZC("SZZZC", "深圳中转仓"),
    CKBG("CKBG", "出口报关"),
    XGQG("XGQG", "香港清关"),
    KYDD("KYDD", "空运订单"),
    ZGYSDD("ZGYSDD","中港运输订单"),
    NLYSDD("NLYSDD","内陆运输订单"),


    //主订单状态
    MAIN_1("1", "正常"),
    MAIN_2("2", "草稿"),
    MAIN_3("3", "关闭"),
    MAIN_4("4", "待补全"),
    MAIN_5("5", "终止"),//废弃
    MAIN_6("6", "待取消处理"),
    MAIN_7("7", "待驳回处理"),

    //所有主/子订单的状态
    CLOSE("CLOSE", "关闭"),
    STOP("STOP", "终止"),//废弃


    //纯报关子订单流程节点+纯报关子订单状态
    CUSTOMS_C_0("C_0", "待接单"), //仅报关子订单状态用
    CUSTOMS_C_1("C_1", "报关接单"),
    CUSTOMS_C_1_1("C_1_1", "报关接单驳回"),
    CUSTOMS_C_2("C_2", "报关打单"),
    CUSTOMS_C_3("C_3", "报关复核"),
    CUSTOMS_C_4("C_4", "报关申报"),
    CUSTOMS_C_5("C_5", "报关放行"),
    CUSTOMS_C_5_1("C_5_1", "报关放行驳回"),//仅报关子订单状态用
    CUSTOMS_C_6("C_6", "通关确认"),
    CUSTOMS_C_6_1("C_6_1", "通关查验"),//仅报关子订单状态用
    CUSTOMS_C_6_2("C_6_2", "通关其他异常"),//仅报关子订单状态用
    CUSTOMS_C_7("C_7", "录入费用"),
    CUSTOMS_C_8("C_8", "费用审核"),
    CUSTOMS_C_Y("C_Y", "报关异常"),//仅报关子订单状态用

    //中港子订单流程节点+中港子订单状态
    TMS_T_0("T_0", "待接单"), //仅中港子订单状态用
    TMS_T_1("T_1", "运输接单"),
    TMS_T_1_1("T_1_1", "运输接单驳回"),
    TMS_T_2("T_2", "运输派车"),
    TMS_T_2_1("T_2_1", "运输派车驳回"),
    TMS_T_3("T_3", "运输审核"),
    TMS_T_3_1("T_3_1", "运输审核不通过"),//仅中港子订单状态用,这个是审核派车信息
    TMS_T_3_2("T_3_2", "运输审核驳回"),//仅中港子订单状态用,这个是驳回可编辑订单
    TMS_T_4("T_4", "确认派车"),
    TMS_T_4_1("T_4_1", "确认派车驳回"),
    TMS_T_5_1("T_5_1", "车辆提货驳回"),
    TMS_T_5("T_5", "车辆提货"),
    TMS_T_6("T_6", "车辆过磅"),
    TMS_T_7("T_7", "通关前审核"),//通关前审核必须先操作外部报关放行
    TMS_T_7_1("T_7_1", "通关前审核不通过"),//仅中港子订单状态用
    TMS_T_8("T_8", "通关前复核"),
    TMS_T_8_1("T_8_1", "通关前复核不通过"),//仅中港子订单状态用
    TMS_T_9("T_9", "车辆通关"),
    TMS_T_9_1("T_9_1", "车辆通关查验"),//仅中港子订单状态用
    TMS_T_9_2("T_9_2", "车辆通关其他异常"),//仅中港子订单状态用
    TMS_T_10("T_10", "车辆入仓"),
    TMS_T_11("T_11", "中转仓卸货"),
    TMS_T_12("T_12", "中转仓装货"),
    TMS_T_13("T_13", "车辆出仓"),
    TMS_T_14("T_14", "车辆派送"),
    TMS_T_15("T_15", "确认签收"),


    //空运订单状态流程节点
    AIR_A_0("A_0", "待接单"),
    AIR_A_1("A_1", "空运接单"),
    AIR_A_1_1("A_1_1", "空运接单驳回"),
    AIR_A_2("A_2", "空运订舱"),
    AIR_A_2_1("A_2_1", "订舱驳回"),
    AIR_A_3("A_3", "订单入仓"),
    AIR_A_3_1("A_3_1", "订单入仓驳回"),
    AIR_A_4("A_4", "确认提单"),
    AIR_A_5("A_5", "确认离港"),
    AIR_A_6("A_6", "确认到港"),
    AIR_A_7("A_7", "海外代理"),
    AIR_A_8("A_8", "确认签收"),

    //外部报关放行
    EXT_CUSTOMS_RELEASE("E_C_0", "外部报关放行"),

    //香港清关
    HK_CLEAR_1("HK_C_1", "香港清关"),

    //内陆运输 TODO

    //费用状态
    COST_0("0", "审核驳回"),
    COST_1("1", "草稿"),
    COST_2("2", "提交审核"),
    COST_3("3", "审核通过");

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

    public static String getCode(String desc) {
        for (OrderStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return "";
    }

    /**
     * 获取空运上个节点
     * 如果是驳回状态就是当前状态
     */
    public static OrderStatusEnum getAirOrderPreStatus(String currentStatus) {

        if (AIR_A_3_1.getCode().equals(currentStatus)) {
            return OrderStatusEnum.AIR_A_3_1;
        }

        List<OrderStatusEnum> statusEnums = getAirOrderProcess();

        for (int i = 0; i < statusEnums.size(); i++) {
            OrderStatusEnum orderStatusEnum = statusEnums.get(i);
            if (orderStatusEnum.getCode().equals(currentStatus)) {
                if (i == 0) {
                    return AIR_A_0;
                }
                return statusEnums.get(i - 1);
            }
        }

        return null;
    }


    /**
     * 获取空运下个节点
     * 如果是驳回状态就是当前状态
     */
    public static OrderStatusEnum getAirOrderNextStatus(String currentStatus) {

        if (AIR_A_2_1.getCode().equals(currentStatus)) {
            return OrderStatusEnum.AIR_A_2_1;
        }
        List<OrderStatusEnum> statusEnums = getAirOrderProcess();

        boolean next = true;
        for (int i = 0; i < statusEnums.size(); i++) {
            OrderStatusEnum orderStatusEnum = statusEnums.get(i);
            if (orderStatusEnum.getCode().equals(currentStatus)) {
                next = false;
                continue;
            }
            if (!next) {
                return orderStatusEnum;
            }
        }

        return null;
    }

    public static List<OrderStatusEnum> getAirOrderProcess() {
        List<OrderStatusEnum> statusEnums = new ArrayList<>();
        statusEnums.add(AIR_A_0);
        statusEnums.add(AIR_A_1);
        statusEnums.add(AIR_A_2);
        statusEnums.add(AIR_A_3);
        statusEnums.add(AIR_A_4);
        statusEnums.add(AIR_A_5);
        statusEnums.add(AIR_A_6);
        statusEnums.add(AIR_A_7);
        statusEnums.add(AIR_A_8);
        return statusEnums;
    }

    public static OrderStatusEnum getAirOrderRejection(String status) {
        if (OrderStatusEnum.AIR_A_0.getCode().equals(status)) {//接单页面驳回
            return AIR_A_1_1;
        }
        if (OrderStatusEnum.AIR_A_1.getCode().equals(status)) {//订舱页面驳回
            return AIR_A_2_1;
        }
        if (OrderStatusEnum.AIR_A_2.getCode().equals(status)) {//入仓页面驳回
            return AIR_A_3_1;
        }
        return null;
    }
}
