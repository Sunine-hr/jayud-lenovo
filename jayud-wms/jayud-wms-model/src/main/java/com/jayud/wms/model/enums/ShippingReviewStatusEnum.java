package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/1/10 10:08
 * @description: 关箱复核状态
 */
@Getter
@AllArgsConstructor
public enum ShippingReviewStatusEnum {

    UNFINISH(0, "未完成"),
    FINISH(1, "已完成"),
    CLOSE_BOX(2, "关箱");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 信息
     */
    private final String msg;
}
