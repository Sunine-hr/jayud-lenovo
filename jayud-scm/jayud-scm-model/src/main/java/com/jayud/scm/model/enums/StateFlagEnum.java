package com.jayud.scm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * 订单状态
 */
@Getter
@AllArgsConstructor
public enum StateFlagEnum {

    //订单状态-1验货异常，-2异常已处理，-3提货异常， 0未确认,1已确认待交货,2已收货,3验货已完成,4入库,5已过关,6已过货,7配送中,8已签收,9部分出库，10已出库，11已报关
    STATE_FLAG_NEGATIVE_1(-1, "验货异常"),
    STATE_FLAG_NEGATIVE_2(-2, "异常已处理"),
    STATE_FLAG_NEGATIVE_3(-3, "提货异常"),
    STATE_FLAG_0(0, "未确认"),
    STATE_FLAG_1(1, "已确认待交货"),
    STATE_FLAG_2(2, "已收货"),
    STATE_FLAG_3(3, "验货已完成"),
    STATE_FLAG_4(4, "入库"),
    STATE_FLAG_5(5, "已过关"),
    STATE_FLAG_6(6, "已过货"),
    STATE_FLAG_7(7, "配送中"),
    STATE_FLAG_8(8, "已签收"),
    STATE_FLAG_9(9, "部分出库"),
    STATE_FLAG_10(10, "已出库"),
    STATE_FLAG_11(11, "已报关")
    ;


    private Integer code;
    private String desc;

    public static String getDesc(String code) {
        for (StateFlagEnum value : values()) {
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
        for (StateFlagEnum stateFlagEnum: StateFlagEnum.values()) {
            Map item= new HashMap<String, Object>();
            item.put("code",stateFlagEnum.code);
            item.put("desc",stateFlagEnum.desc);
            list.add(item);
        }
        return list;
    }


}
