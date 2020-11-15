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
    THREE("3", OrderStatusEnum.TMS_T_13.getCode().split("_")[1], "货物派送"),
    FOUR("4", OrderStatusEnum.TMS_T_14.getCode().split("_")[1], "订单签收"),
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

            Integer num = Integer.parseInt(value.getStep());
            if (isTransferBin) {
                if (value.equals(DriverFeedbackStatusEnum.FOUR)) {
                    //订单签收替换成入仓(送到中转仓，入仓代表订单签收)
                    num = Integer.parseInt(OrderStatusEnum.TMS_T_9.getCode().split("_")[1]);
                }
            }
            //获取下一步
            DriverFeedbackStatusEnum nextStepEnum = i == tmps.size() - 1 ? value : tmps.get(i + 1);

            Integer nextNum = Integer.parseInt(nextStepEnum.getStep());

            Map<String, Object> tmp = new HashMap<>();
            tmp.put("id", value.getCode());
            tmp.put("value", value.getDesc());
            boolean currentState = false;
            boolean isEdit = false;
            //寻找操作流程节点
            if (num.equals(statusNum)) {
                if (statusStr.length > 2) {
                    isEdit = false;
                } else {
                    isEdit = true; //可能进行驳回流程
                }
                currentState = true;
            } else if (num < statusNum && nextNum > statusNum) {//区间查询
                currentState = true;
                isEdit = false;
            } else if (num.equals(nextNum) && statusNum > num) {
                currentState = true;
                isEdit = false;
            }

            tmp.put("currentState", currentState);
            tmp.put("isEdit", isEdit);
            list.add(tmp);
            if (isGetNode && currentState) {
                break;
            }
        }

        return list;
    }

    /**
     * 送中转仓流程
     */
//    private static void transferWarehouseProcess(Integer num, Integer nextNum, DriverFeedbackStatusEnum statusEnum,
//                                                 Integer statusNum, Map<String, Object> map) {
//
//        Integer step = Integer.parseInt(DriverFeedbackStatusEnum.FOUR.getStep());
//        //送到中转仓仓库,车辆入仓代表订单签收
//        if (statusEnum.equals(DriverFeedbackStatusEnum.FOUR)) {
//            num = Integer.parseInt(OrderStatusEnum.TMS_T_9.getCode().split("_")[1]);
//
//        }
//
//        boolean currentState = false;
//        boolean isEdit = false;
//        if (num.equals(statusNum)) {
//            currentState = true;
//            isEdit = true;
//        } else if (num > statusNum && nextNum < statusNum) {//区间查询
//            currentState = true;
//            isEdit = false;
//        } else if (statusNum>){
//
//        }
//
//        map.put("currentState", currentState);
//        map.put("isEdit", isEdit);
//    }
    public static String getDesc(String code) {
        for (DriverFeedbackStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }
}
