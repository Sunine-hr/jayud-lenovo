package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2021/12/23 13:50
 * @description: 出库订单--订单状态枚举
 */
@Getter
@AllArgsConstructor
public enum OutboundOrdertSatus {

    UNASSIGNED(1,"未分配"),

    ASSIGNED(2,"已分配"),

    OUT_STOCK(3,"缺货中"),

    ISSUED(4,"已出库");


    /**
     * 类型
     */
    private final Integer type;

    /**
     * 信息
     */
    private final String msg;
}
