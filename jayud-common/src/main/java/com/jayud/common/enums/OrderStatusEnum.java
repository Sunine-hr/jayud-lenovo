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
    ZGYSDD("ZGYSDD", "中港运输订单"),
    FWD("FWD", "服务单"),
    FWDDD("FWDDD", "服务单订单"),
    HY("HY", "海运"),
    HYDD("HYDD", "海运订单"),
    NLDD("NLDD", "内陆运输订单"),
    TC("TC","拖车"),
    TCEDD("TCEDD","拖车出口订单"),
    TCIDD("TCIDD","拖车进口订单"),

    //主订单状态
    MAIN_1("1", "正常"),
    MAIN_2("2", "草稿"),
    MAIN_3("3", "关闭"),
    MAIN_4("4", "待补全"),
    MAIN_5("5", "终止"),//废弃
    MAIN_6("6", "待取消"),
    MAIN_7("7", "待驳回"),
    MAIN_8("8", "待处理"),

    //所有主/子订单的状态
    CLOSE("CLOSE", "关闭"),
    STOP("STOP", "终止"),//废弃


    //纯报关子订单流程节点+纯报关子订单状态
    CUSTOMS_C_0("C_0", "待接单"), //仅报关子订单状态用
    CUSTOMS_C_1("C_1", "报关接单"),
    CUSTOMS_C_1_1("C_1_1", "报关接单驳回"),
    CUSTOMS_C_2("C_2", "报关打单"),
    CUSTOMS_C_3("C_3", "报关复核"),
    CUSTOMS_C_9("C_9", "报关二复"),
    CUSTOMS_C_11("C_11", "申报舱单"),
    CUSTOMS_C_4("C_4", "报关申报"),
    CUSTOMS_C_10("C_10", "报关放行"),
    CUSTOMS_C_5("C_5", "放行审核"),
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
    TMS_T_5("T_5", "车辆提货"),
    TMS_T_5_1("T_5_1", "车辆提货驳回"),
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
    AIR_A_3("A_3", "订单入舱"),
    AIR_A_3_1("A_3_1", "订单入仓驳回"),
    AIR_A_3_2("A_3_2", "订舱驳回编辑"),
    AIR_A_4("A_4", "确认提单"),
    AIR_A_5("A_5", "确认离港"),
    AIR_A_6("A_6", "确认到港"),
    AIR_A_7("A_7", "海外代理"),
    AIR_A_8("A_8", "确认签收"),

    //海运订单状态流程节点
    SEA_S_0("S_0", "待接单"),
    SEA_S_1("S_1", "海运接单"),
    SEA_S_1_1("S_1_1", "海运接单驳回"),
    SEA_S_2("S_2", "海运订船"),
    SEA_S_2_1("S_2_1", "订船驳回"),
    SEA_S_3("S_3", "确认入仓"),
    SEA_S_3_1("S_3_1", "订单入仓驳回"),
    SEA_S_3_2("S_3_2", "订船驳回编辑"),
    SEA_S_4("S_4", "提交补料"),
    SEA_S_5("S_5", "提单草稿确认"),
    SEA_S_6("S_6", "确认装船"),
    SEA_S_7("S_7", "放单确认"),
    SEA_S_8("S_8", "确认到港"),
    SEA_S_9("S_9", "海外代理"),
    SEA_S_10("S_10", "订单签收"),

    //内陆运输
    INLANDTP_NL_0("NL_0", "待接单"),
    INLANDTP_NL_1("NL_1", "内陆接单"),
    INLANDTP_NL_1_1("NL_1_1", "内陆接单驳回"),
    INLANDTP_NL_2("NL_2", "内陆派车"),
    INLANDTP_NL_2_1("NL_2_1", "内陆派车驳回"),
    INLANDTP_NL_3("NL_3", "派车审核"),
    INLANDTP_NL_3_1("NL_3_1", "派车审核不通过"),
    INLANDTP_NL_3_2("NL_3_2", "派车审核驳回"),
    INLANDTP_NL_4("NL_4", "确认派车"),
    INLANDTP_NL_4_1("NL_4_1", "确认派车驳回"),
    INLANDTP_NL_5("NL_5", "车辆提货"),
    INLANDTP_NL_5_1("NL_5_1", "车辆提货驳回"),
    INLANDTP_NL_6("NL_6", "货物签收"),
    //拖车订单状态流程节点
    TT_0("TT_0","待接单"),
    TT_1("TT_1","拖车接单"),
    TT_1_1("TT_1_1","拖车接单驳回"),
    TT_2("TT_2","拖车派车"),
    TT_2_1("TT_2_1","拖车派车驳回"),
    TT_3("TT_3","派车审核"),
    TT_3_1("TT_3_1","派车审核驳回"),
    TT_3_2("TT_3_2","派车驳回调度"),
    TT_4("TT_4","拖车提柜"),
    TT_4_1("TT_4_1","拖车提柜驳回"),
    TT_5("TT_5","拖车到仓"),
    TT_6("TT_6","拖车离仓"),
    TT_7("TT_7","拖车过磅"),
    TT_8("TT_8","确认还柜"),





    //外部报关放行
    EXT_CUSTOMS_RELEASE("E_C_0", "外部报关放行"),

    //香港清关
    HK_CLEAR_1("HK_C_1", "香港清关"),


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

    public static OrderStatusEnum getEnums(String code) {
        for (OrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static String getCode(String desc) {
        for (OrderStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return "";
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

    public static List<OrderStatusEnum> getCustomsProcess() {
        List<OrderStatusEnum> statusEnums = new ArrayList<>();
        statusEnums.add(CUSTOMS_C_0);
        statusEnums.add(CUSTOMS_C_1);
        statusEnums.add(CUSTOMS_C_2);
        statusEnums.add(CUSTOMS_C_3);
        statusEnums.add(CUSTOMS_C_9);
        statusEnums.add(CUSTOMS_C_11);
        statusEnums.add(CUSTOMS_C_4);
        statusEnums.add(CUSTOMS_C_10);
        statusEnums.add(CUSTOMS_C_5);
        statusEnums.add(CUSTOMS_C_6);
        return statusEnums;
    }


    public static List<OrderStatusEnum> getInlandTPProcess() {
        List<OrderStatusEnum> statusEnums = new ArrayList<>();
        statusEnums.add(INLANDTP_NL_0);
        statusEnums.add(INLANDTP_NL_1);
        statusEnums.add(INLANDTP_NL_2);
        statusEnums.add(INLANDTP_NL_3);
        statusEnums.add(INLANDTP_NL_4);
        statusEnums.add(INLANDTP_NL_5);
        statusEnums.add(INLANDTP_NL_6);
        return statusEnums;
    }


    /**
     * 获取空运上个节点
     * 如果是驳回状态就是当前状态
     */
    public static OrderStatusEnum getAirOrderPreStatus(String currentStatus) {
        if (AIR_A_3_2.getCode().equals(currentStatus)) {
            return OrderStatusEnum.AIR_A_3_2;
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

    /**
     * 获取报关下个节点
     * 如果是驳回状态就是当前状态
     */
    public static OrderStatusEnum getCustomsOrderNextStatus(String currentStatus) {
        if (CUSTOMS_C_5_1.getCode().equals(currentStatus)) {
            return OrderStatusEnum.CUSTOMS_C_5_1;
        }
        List<OrderStatusEnum> statusEnums = getCustomsProcess();
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


    /**
     * 获取海运下个节点
     * 如果是驳回状态就是当前状态
     */
    public static OrderStatusEnum getSeaOrderNextStatus(String currentStatus) {

        if (SEA_S_3_2.getCode().equals(currentStatus)) {
            return OrderStatusEnum.SEA_S_3_2;
        }
        List<OrderStatusEnum> statusEnums = getSeaOrderProcess();
        for (OrderStatusEnum statusEnum : statusEnums) {
            if (statusEnum.getCode().equals(currentStatus)) {
                return statusEnum;
            }
        }

        return null;
    }

    public static List<OrderStatusEnum> getSeaOrderProcess() {
        List<OrderStatusEnum> statusEnums = new ArrayList<>();
        statusEnums.add(SEA_S_0);
        statusEnums.add(SEA_S_1);
        statusEnums.add(SEA_S_2);
        statusEnums.add(SEA_S_3);
        statusEnums.add(SEA_S_4);
        statusEnums.add(SEA_S_5);
        statusEnums.add(SEA_S_6);
        statusEnums.add(SEA_S_7);
        statusEnums.add(SEA_S_8);
        statusEnums.add(SEA_S_9);
        statusEnums.add(SEA_S_10);
        return statusEnums;
    }

    public static OrderStatusEnum getSeaOrderRejection(String status) {
        if (OrderStatusEnum.SEA_S_0.getCode().equals(status)) {//接单页面驳回
            return SEA_S_1_1;
        }
        if (OrderStatusEnum.SEA_S_1.getCode().equals(status)) {//订舱页面驳回
            return SEA_S_2_1;
        }
        if (OrderStatusEnum.SEA_S_2.getCode().equals(status)) {//入仓页面驳回
            return SEA_S_3_1;
        }
        return null;
    }


    /**
     * 获取拖车下个节点
     * 如果是驳回状态就是当前状态
     */
    public static OrderStatusEnum getTrailerOrderNextStatus(String currentStatus) {

        if (TT_3_2.getCode().equals(currentStatus)) {
            return OrderStatusEnum.TT_3_2;
        }
        List<OrderStatusEnum> statusEnums = getTrailerOrderProcess();
        for (OrderStatusEnum statusEnum : statusEnums) {
            if(statusEnum.getCode().equals(currentStatus)){
                return statusEnum;
            }
        }

        return null;
    }

    public static List<OrderStatusEnum> getTrailerOrderProcess() {
        List<OrderStatusEnum> statusEnums = new ArrayList<>();
        statusEnums.add(TT_0);
        statusEnums.add(TT_1);
        statusEnums.add(TT_2);
        statusEnums.add(TT_3);
        statusEnums.add(TT_4);
        statusEnums.add(TT_5);
        statusEnums.add(TT_6);
        statusEnums.add(TT_7);
        statusEnums.add(TT_8);
        return statusEnums;
    }

    public static OrderStatusEnum getTrailerOrderRejection(String status) {
        if (OrderStatusEnum.TT_0.getCode().equals(status)) {//接单页面驳回
            return TT_1_1;
        }
        if (OrderStatusEnum.TT_1.getCode().equals(status)) {//派车页面驳回
            return TT_2_1;
        }
        if (OrderStatusEnum.TT_2.getCode().equals(status)) {//派车审核页面驳回
            return TT_3_1;
        }
        if (OrderStatusEnum.TT_3.getCode().equals(status)) {//派车审核页面驳回
            return TT_4_1;
        }
        return null;
    }


    /**
     * 获取驳回枚举
     */
    public static String getRejectionStatus(String status, String subOrderSigns) {
        String[] rejectionStatus = getRejectionStatus(subOrderSigns);
        if (rejectionStatus == null) {
            return null;
        }
        for (String tmp : rejectionStatus) {
            if (tmp.equals(status)) {
                return tmp;
            }
        }
        return null;
    }

    /**
     * 获取驳回状态
     */
    public static String[] getRejectionStatus(String... subOrderSigns) {
        if (subOrderSigns == null) {
            return new String[]{CUSTOMS_C_1_1.getCode(), CUSTOMS_C_5_1.getCode(),
                    TMS_T_1_1.getCode(), TMS_T_2_1.getCode(), TMS_T_3_1.getCode(),
                    TMS_T_3_2.getCode(), TMS_T_4_1.getCode(), TMS_T_5_1.getCode(),
                    AIR_A_1_1.getCode(), AIR_A_2_1.getCode(), AIR_A_3_1.getCode(),
                    AIR_A_3_2.getCode(), SEA_S_1_1.getCode(), SEA_S_2_1.getCode(),
                    SEA_S_3_1.getCode(), SEA_S_3_2.getCode(),
                    INLANDTP_NL_1_1.getCode(), INLANDTP_NL_2_1.getCode(),
                    INLANDTP_NL_3_1.getCode(), INLANDTP_NL_3_2.getCode(),
                    INLANDTP_NL_4_1.getCode(), INLANDTP_NL_5_1.getCode(),
                    TT_1_1.getCode(),TT_2_1.getCode(),TT_3_1.getCode(),TT_3_2.getCode(),TT_4_1.getCode()};
        }
        for (String subOrderSign : subOrderSigns) {
            //todo 有需要再补
            if (SubOrderSignEnum.ZGYS.getSignOne().equals(subOrderSign)) {
                return new String[]{
                        TMS_T_1_1.getCode(), TMS_T_2_1.getCode(),
                        TMS_T_3_2.getCode(), TMS_T_4_1.getCode(), TMS_T_5_1.getCode()};
            }
            if (SubOrderSignEnum.KY.getSignOne().equals(subOrderSign)) {
                return new String[]{
                        AIR_A_1_1.getCode(), AIR_A_2_1.getCode(), AIR_A_3_1.getCode(),
                        AIR_A_3_2.getCode()};
            }
            if (SubOrderSignEnum.BG.getSignOne().equals(subOrderSign)) {
                return new String[]{CUSTOMS_C_1_1.getCode()};
            }
            if (SubOrderSignEnum.HY.getSignOne().equals(subOrderSign)) {
                return new String[]{
                        SEA_S_1_1.getCode(), SEA_S_2_1.getCode(), SEA_S_3_1.getCode(), SEA_S_3_2.getCode()
                };
            }
            if (SubOrderSignEnum.NL.getSignOne().equals(subOrderSign)) {
                return new String[]{
                        INLANDTP_NL_1_1.getCode(), INLANDTP_NL_2_1.getCode(), INLANDTP_NL_3_1.getCode(),
                        INLANDTP_NL_3_2.getCode(), INLANDTP_NL_4_1.getCode(), INLANDTP_NL_5_1.getCode()
                };
            }
            if(SubOrderSignEnum.TC.getSignOne().equals(subOrderSign)){
                return new String[]{
                        TT_1_1.getCode(),TT_2_1.getCode(),TT_3_1.getCode(),TT_3_2.getCode(),TT_4_1.getCode()
                };
            }
        }
        return null;
    }

    public static List<OrderStatusEnum> getInlandTPStatus(boolean isAll) {
        List<OrderStatusEnum> statusEnums = new ArrayList<>();
        statusEnums.add(INLANDTP_NL_0);
        statusEnums.add(INLANDTP_NL_1);
        if (isAll) statusEnums.add(INLANDTP_NL_1_1);
        statusEnums.add(INLANDTP_NL_2);
        if (isAll) statusEnums.add(INLANDTP_NL_2_1);
        statusEnums.add(INLANDTP_NL_3);
        if (isAll) statusEnums.add(INLANDTP_NL_3_1);
        if (isAll) statusEnums.add(INLANDTP_NL_3_2);
        statusEnums.add(INLANDTP_NL_4);
        statusEnums.add(INLANDTP_NL_5);
        if (isAll) statusEnums.add(INLANDTP_NL_5_1);
        statusEnums.add(INLANDTP_NL_6);
        return statusEnums;
    }

    /**
     * 获取内陆运输上个节点
     * 如果是驳回状态就是当前状态
     */
    public static OrderStatusEnum getInlandTPOrderPreStatus(String currentStatus) {
        if (INLANDTP_NL_3_1.getCode().equals(currentStatus)) {
            return OrderStatusEnum.INLANDTP_NL_3_1;
        }
        List<OrderStatusEnum> statusEnums = getInlandTPStatus(false);
        for (int i = 0; i < statusEnums.size(); i++) {
            OrderStatusEnum orderStatusEnum = statusEnums.get(i);
            if (orderStatusEnum.getCode().equals(currentStatus)) {
                if (i == 0) {
                    return INLANDTP_NL_0;
                }
                return statusEnums.get(i - 1);
            }
        }
        return null;
    }

    public static OrderStatusEnum getInlandTPOrderRejection(String status) {
        if (OrderStatusEnum.INLANDTP_NL_0.getCode().equals(status)) {//接单页面驳回
            return INLANDTP_NL_1_1;
        }
        if (OrderStatusEnum.INLANDTP_NL_1.getCode().equals(status)) {//派车页面驳回
            return INLANDTP_NL_2_1;
        }
        if (OrderStatusEnum.INLANDTP_NL_2.getCode().equals(status)) {//派车审核页面驳回
            return INLANDTP_NL_3_2;
        }
        if (OrderStatusEnum.INLANDTP_NL_3.getCode().equals(status)) {//确认派车页面驳回
            return INLANDTP_NL_4_1;
        }
        if (OrderStatusEnum.INLANDTP_NL_4.getCode().equals(status)) {//车辆提货页面驳回
            return INLANDTP_NL_5_1;

        }
        return null;
    }

}
