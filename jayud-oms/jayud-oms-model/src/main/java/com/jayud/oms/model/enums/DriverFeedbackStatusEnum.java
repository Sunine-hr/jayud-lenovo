package com.jayud.oms.model.enums;

import com.jayud.common.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
public enum DriverFeedbackStatusEnum {

    //待提货 0(T_4)，待过磅 1,2,3,4(T_5)，待车辆通关5,6,7,8,9,10,11,12(T_8)，待货物派送13(T_13),待签收14,15(待签收)
    //待提货 0(T_4)，待过磅 1,2,3,4(T_5)，待车辆通关5,6,7,8(T_8),待签收 9(车辆入仓)
    ZERO("0", "0", "车辆提货"),
    ONE("1", "1-4", "车辆过磅"),
    TWO("2", "5-12_5-8", "车辆通关"),
    THREE("3", "13", "货物派送"),
    FOUR("4", "14-15_9-15", "订单签收"),

    ;

    private String code;
    private String step;
    private String desc;


    /**
     * 构造流程
     */
    public static List<Map<String, Object>> constructionProcess(String status,
                                                                DriverFeedbackStatusEnum exclude, boolean isGetNode) {
        int processNode = getProcessNode(status);
        ArrayList<DriverFeedbackStatusEnum> tmps = new ArrayList<>(Arrays.asList(values()));
        boolean isTransferBin = false;
        //排除步骤
        if (exclude != null) {
            if (THREE.equals(exclude)) {//送到中转仓库不需要货物派送
                isTransferBin = true;
            }
            tmps.remove(exclude);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < tmps.size(); i++) {
            DriverFeedbackStatusEnum value = tmps.get(i);
            String[] steps = value.getStep().split("_");
            String step = steps[0];
            if (isTransferBin) {
                if (steps.length > 1) {
                    step = steps[1];
                }
            }
            //取值范围
            String[] ranges = step.split("-");
            int rangeOne = Integer.parseInt(ranges[0]);
            int rangeTwo = ranges.length > 1 ? Integer.parseInt(ranges[1]) : rangeOne;
            //取节点
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("id", value.getCode());
            tmp.put("value", value.getDesc());
            boolean currentState = false;
            boolean isEdit = false;
            if (rangeOne == processNode) {
                currentState = true;
                isEdit = true;
            } else if (rangeOne < processNode && rangeTwo >= processNode) {
                currentState = true;
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

    public static String getDesc(String code) {
        for (DriverFeedbackStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }


    //待提货 0(T_4)，待过磅 1,2,3,4(T_5)，待车辆通关5,6,7,8,9,10,11,12(T_8)，待货物派送13(T_13),待签收14,15(待签收)
    //待提货 0(T_4)，待过磅 1,2,3,4(T_5)，待车辆通关5,6,7,8(T_8),待签收 9(车辆入仓)
    public static int getProcessNode(String status) {

        List<String> processList = new ArrayList<>();
        //有货物派送流程
        processList.add(OrderStatusEnum.TMS_T_4.getCode());
        processList.add(OrderStatusEnum.TMS_T_5.getCode());
        processList.add(OrderStatusEnum.TMS_T_6.getCode());
        processList.add(OrderStatusEnum.TMS_T_7.getCode());
        processList.add(OrderStatusEnum.TMS_T_7_1.getCode());
        processList.add(OrderStatusEnum.TMS_T_8.getCode());
        processList.add(OrderStatusEnum.TMS_T_8_1.getCode());
        processList.add(OrderStatusEnum.TMS_T_9_1.getCode());
        processList.add(OrderStatusEnum.TMS_T_9_2.getCode());
        processList.add(OrderStatusEnum.TMS_T_9.getCode());
        processList.add(OrderStatusEnum.TMS_T_10.getCode());
        processList.add(OrderStatusEnum.TMS_T_11.getCode());
        processList.add(OrderStatusEnum.TMS_T_12.getCode());
        processList.add(OrderStatusEnum.TMS_T_13.getCode());
        processList.add(OrderStatusEnum.TMS_T_14.getCode());
        processList.add(OrderStatusEnum.TMS_T_15.getCode());

        for (int i = 0; i < processList.size(); i++) {
            String node = processList.get(i);
            if (node.equals(status)) {
                return i;
            }
        }
        return 0;
    }


}
