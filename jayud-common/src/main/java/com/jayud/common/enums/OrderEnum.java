package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * nanjing mall 订单状态枚举
 * 南京商城
 */
@Getter
@AllArgsConstructor
public enum OrderEnum {
    //订单状态枚举

    //订单前端主状态
    FRONT_DRAFT("0", "草稿"),
    FRONT_UPDATE("9", "补资料"),
    FRONT_PLACED("10", "已下单"),
    FRONT_RECEIVED("20", "已收货"),
    FRONT_TRANSIT("30", "转运中"),
    FRONT_SIGNED("40", "已签收"),
    FRONT_FINISH("50", "已完成"),
    FRONT_CANCEL("-1", "已取消"),
    FRONT_IMPORT("-2", "旧系统导入"),

    //订单后端主状态
    AFTER_DRAFT("0", "草稿"),
    AFTER_UPDATE("9", "补资料"),
    AFTER_PLACED("10", "已下单"),
    AFTER_RECEIVED("20", "已收货"),
    AFTER_AFFIRM("30", "订单确认"),
    AFTER_TRANSIT("31", "转运中"),
    AFTER_SIGNED("40", "已签收"),
    AFTER_FINISH("50", "已完成"),
    AFTER_CANCEL("-1", "已取消"),
    AFTER_IMPORT("-2", "旧系统导入"),

    //订单小状态，内部状态，非流程状态
    IS_AUDIT_ORDER("is_audit_order", "是否审核单据(1已审单 2未审单)"),
    IS_CONFIRM_BILLING("is_confirm_billing", "是否确认计费重(1已确认 2为确认)")
    ;

    private String code;
    private String name;
}
