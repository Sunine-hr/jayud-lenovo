package com.jayud.customs.model.enums;

import com.jayud.customs.model.po.OrderCustoms;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Enumeration;
import java.util.Objects;
import java.util.function.Function;

/**
 * 云报关状态对应oms系统状态
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum BGOrderStatusEnum implements Enumeration<BGOrderStatusEnum> {

    CUSTOMS_C_2("C_2", "报关打单", "逻辑审核"){
        @Override
        public BGOrderStatusEnum nextElement() {
            return CUSTOMS_C_3;
        }
    },
    CUSTOMS_C_3("C_3", "报关复核","复核"){
        @Override
        public BGOrderStatusEnum nextElement() {
            return CUSTOMS_C_9;
        }
    },
    CUSTOMS_C_9("C_9", "报关二复","总复核"){
        @Override
        public BGOrderStatusEnum nextElement() {
            return CUSTOMS_C_11;
        }
    },
    CUSTOMS_C_11("C_11", "申报舱单","导入舱单"){
        @Override
        public BGOrderStatusEnum nextElement() {
            return CUSTOMS_C_4;
        }
    },
    CUSTOMS_C_4("C_4", "报关申报","通关无纸化审结"){
        @Override
        public BGOrderStatusEnum nextElement() {
            return CUSTOMS_C_10;
        }
    },
    CUSTOMS_C_10("C_10", "报关放行","海关已放行"){
        @Override
        public boolean hasMoreElements() {
            return false;
        }
    };

    private String code;
    private String desc;
    private String status;

    public static String getDesc(String code) {
        for (BGOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return null;
    }

    public static String getCode(String desc) {
        for (BGOrderStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return null;
    }

    public static String getStatus(String desc) {
        for (BGOrderStatusEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getStatus();
            }
        }
        return null;
    }

    public static String getCode1(String status) {
        for (BGOrderStatusEnum value : values()) {
            if (Objects.equals(status, value.getStatus())) {
                return value.getCode();
            }
        }
        return null;
    }

    public static String getDesc1(String status) {
        for (BGOrderStatusEnum value : values()) {
            if (Objects.equals(status, value.getStatus())) {
                return value.getDesc();
            }
        }
        return null;
    }

    public static BGOrderStatusEnum getEnum(String code) {
        for (BGOrderStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    @Override
    public boolean hasMoreElements() {
        return true;
    }

    @Override
    public BGOrderStatusEnum nextElement() {
        return null;
    }

    public void updateProcessStatus(OrderCustoms orderCustoms, Function<OrderCustoms,Boolean> func){
        orderCustoms.setStatus(code);
        //更新状态
        if(!func.apply(orderCustoms)){
            log.warn("更新{}状态失败",desc);
        }
    }
}
