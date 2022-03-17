package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2021/12/22 17:53
 * @description: 出库通知单--订单状态枚举
 */
@Getter
@AllArgsConstructor
public enum NoticeOrdertSatus {

    CREATE(1,"新建"),

    UP_CANCEL(2,"上游取消"),

    TO_OUT(3,"转出库");


    /**
     * 类型
     */
    private final Integer type;

    /**
     * 信息
     */
    private final String msg;
}
