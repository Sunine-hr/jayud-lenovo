package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
@AllArgsConstructor
public enum OrderEnum {

    //订单状态枚举
    CANCELED(-1, "已取消 查看详情"),
    DRAFT(0, "草稿-----提交、取消、查看订单详情（后台不记录数据）"),
    PLACED_AN_ORDER(10, "已下单：编辑、查看订单详情 "),
    RECEIVED(20, "已收货：编辑、查看订单详情"),
    ORDER_CONFIRMATION(30, "订单确认：确认计柜重（不可修改订单信息）"),
    IN_TRANSIT(40, "转运中：查看订单详情"),
    HAVE_BEEN_SIGNED(50, "已签收：账单确认、查看订单详情")
    ;

    private int code;
    private String name;

}
