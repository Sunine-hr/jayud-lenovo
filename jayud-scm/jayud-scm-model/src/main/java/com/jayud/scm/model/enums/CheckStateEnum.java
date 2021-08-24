package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * 订单状态
 */
@Getter
@AllArgsConstructor
public enum CheckStateEnum {

    //状态(0:未提交1:已提交2:已取消3:提货完成4:验货异常5:验货完成6:已入库7:异常已处理)
    CHECK_STATE_0(0, "未提交"),
    CHECK_STATE_1(1, "已提交"),
    CHECK_STATE_2(2, "已取消"),
    CHECK_STATE_3(3, "提货完成"),
    CHECK_STATE_4(4, "验货异常"),
    CHECK_STATE_5(5, "验货完成"),
    CHECK_STATE_6(6, "已入库"),
    CHECK_STATE_7(7, "异常已处理"),
    ;


    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (CheckStateEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    /**
     * 请求返回的Enum集合数据
     * @return
     */
    public static List<Map<String, Object>> getStateFlagEnumList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CheckStateEnum stateFlagEnum: CheckStateEnum.values()) {
            Map item= new HashMap<String, Object>();
            item.put("code",stateFlagEnum.code);
            item.put("desc",stateFlagEnum.desc);
            list.add(item);
        }
        return list;
    }


}
