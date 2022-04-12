package com.jayud.wms.model.enums.LargeScreen;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/4/12 10:00
 * @description: 工单类型
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    RECEIPT_NOTICE(1,"入仓通知单"),
    RECEIPT(2,"入库单"),
    QUALITY(3,"质检单"),
    OUTBOUND_NOTICE(4,"出仓通知单"),
    OUTBOUND_ORDER(5,"出库单"),
    SHIPPING_REVIEW(6,"发运复核单");

    /**
     * 类型
     */
    private Integer type;
    /**
     * 详情
     */
    private String desc;




}
