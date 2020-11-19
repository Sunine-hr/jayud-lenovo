package com.jayud.oms.model.enums;

import com.jayud.common.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
public enum DriverFeedbackStatusEnum {

    ZERO("0", OrderStatusEnum.TMS_T_4.getCode().split("_")[1], "车辆提货"),
    ONE("1", OrderStatusEnum.TMS_T_5.getCode().split("_")[1], "车辆过磅"),
    TWO("2", OrderStatusEnum.TMS_T_8.getCode().split("_")[1], "车辆通关"),
    THREE("3", OrderStatusEnum.TMS_T_9.getCode().split("_")[1]
            + "-" + OrderStatusEnum.TMS_T_13.getCode().split("_")[1], "货物派送"),
    FOUR("4", OrderStatusEnum.TMS_T_14.getCode().split("_")[1], "订单签收"),


    //后台审核状态
//    AUDIT_TWO_CHECK("2", OrderStatusEnum.TMS_T_9_1.getCode().split("_")[1], "车辆通关查验"),
//    AUDIT_TWO_ERROR("2", OrderStatusEnum.TMS_T_9_2.getCode().split("_")[1], "车辆通关其他异常"),
    ;

    private String code;
    private String step;
    private String desc;


    /**
     * 构造流程
     */
    public static List<Map<String, Object>> constructionProcess(String status,
                                                                DriverFeedbackStatusEnum exclude, boolean isGetNode) {


        //订单当前状态
        String[] statusStr = status.split("_");
        Integer statusNum = Integer.parseInt(statusStr[1]);
        List<Map<String, Object>> list = new ArrayList<>();

        ArrayList<DriverFeedbackStatusEnum> tmps = new ArrayList<>(Arrays.asList(values()));

        //是否送到中转仓
        boolean isTransferBin = false;
        //排除步骤
        if (exclude != null) {
            if (THREE.equals(exclude)) {//送到中转仓库不需要货物派送
                isTransferBin = true;
            }
            tmps.remove(exclude);
        }

        for (int i = 0; i < tmps.size(); i++) {
            //当前步骤
            DriverFeedbackStatusEnum value = tmps.get(i);
            String[] steps = value.getStep().split("-");
            Integer num = Integer.parseInt(steps[0]);
            //获取下一步
            DriverFeedbackStatusEnum nextStepEnum = i == tmps.size() - 1 ? value : tmps.get(i + 1);
            Integer nextNum = Integer.parseInt(nextStepEnum.getStep());

            if (isTransferBin) {
                if (value.equals(DriverFeedbackStatusEnum.FOUR)) {
                    //订单签收替换成入仓(送到中转仓，入仓代表订单签收)
                    num = Integer.parseInt(OrderStatusEnum.TMS_T_9.getCode().split("_")[1]);
                }
                if (value.equals(DriverFeedbackStatusEnum.TWO)) {
                    nextNum = Integer.parseInt(OrderStatusEnum.TMS_T_9.getCode().split("_")[1]);
                }
            }


            Map<String, Object> tmp = new HashMap<>();
            tmp.put("id", value.getCode());
            tmp.put("value", value.getDesc());
            boolean currentState = false;
            boolean isEdit = false;
            //寻找操作流程节点
            if (num.equals(statusNum) || (steps.length > 1 && steps[1].equals(statusNum))) {
                if (statusStr.length > 2) {
                    //如何存在审核，应该停留在上一个状态，并且修改当前状态
                    list.get(i - 1).put("currentState", true);
                    isEdit = false;
                } else {
                    isEdit = true;
                    currentState = true;
                }
            } else if (num < statusNum && nextNum > statusNum) {//区间查询
                currentState = true;
                isEdit = false;
            } else if (num.equals(nextNum) && statusNum > num) {
                currentState = true;
                isEdit = false;
            }

            tmp.put("currentState", currentState);
            tmp.put("isEdit", isEdit);
            if (isGetNode) {
                if (currentState) {
                    list.add(tmp);
                    break;
                }
            } else {
                list.add(tmp);
            }


        }

        return list;
    }
//    public static List<Map<String, Object>> constructionProcess(String status,
//                                                                DriverFeedbackStatusEnum exclude, boolean isGetNode) {
//
//
//        //订单当前状态
//        String[] statusStr = status.split("_");
//        Integer statusNum = Integer.parseInt(statusStr[1]);
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        ArrayList<DriverFeedbackStatusEnum> tmps = new ArrayList<>(Arrays.asList(values()));
//
//        //是否送到中转仓
//        boolean isTransferBin = false;
//        //排除步骤
//        if (exclude != null) {
//            if (THREE.equals(exclude)) {//送到中转仓库不需要货物派送
//                isTransferBin = true;
//            }
//            tmps.remove(exclude);
//        }
//
//        for (int i = 0; i < tmps.size(); i++) {
//            //当前步骤
//
//
//        }
//
//        return list;
//    }
//
//    public static String getDesc(String code) {
//        for (DriverFeedbackStatusEnum value : values()) {
//            if (Objects.equals(code, value.getCode())) {
//                return value.getDesc();
//            }
//        }
//        return "";
//    }


    //待提货 0(T_4)，待过磅 1,2,3,4(T_5)，待车辆通关5,6,7,8,9,10,11,12(T_8)，待货物派送13(T_13),待签收14,15(待签收)
    //待提货 0(T_4)，待过磅 1,2,3,4(T_5)，待车辆通关5,6,7(T_8),待签收 9(车辆入仓)
//    public int getProcessNode(String status) {
//
//        List<String> processList = new ArrayList<>();
//        //有货物派送流程
//        processList.add(OrderStatusEnum.TMS_T_4.getCode());
//        processList.add(OrderStatusEnum.TMS_T_5.getCode());
//        processList.add(OrderStatusEnum.TMS_T_6.getCode());
//        processList.add(OrderStatusEnum.TMS_T_7.getCode());
//        processList.add(OrderStatusEnum.TMS_T_7_1.getCode());
//        processList.add(OrderStatusEnum.TMS_T_8.getCode());
//        processList.add(OrderStatusEnum.TMS_T_8_1.getCode());
//        processList.add(OrderStatusEnum.TMS_T_9_1.getCode());
//        processList.add(OrderStatusEnum.TMS_T_9_2.getCode());
//        processList.add(OrderStatusEnum.TMS_T_9.getCode());
//        processList.add(OrderStatusEnum.TMS_T_10.getCode());
//        processList.add(OrderStatusEnum.TMS_T_11.getCode());
//        processList.add(OrderStatusEnum.TMS_T_12.getCode());
//        processList.add(OrderStatusEnum.TMS_T_13.getCode());
//        processList.add(OrderStatusEnum.TMS_T_14.getCode());
//        processList.add(OrderStatusEnum.TMS_T_15.getCode());
//
//        for (int i = 0; i < processList.size(); i++) {
//            String node = processList.get(i);
//            if (node.equals(status)) {
//                return i;
//            }
//        }
//
//
//    }


}
