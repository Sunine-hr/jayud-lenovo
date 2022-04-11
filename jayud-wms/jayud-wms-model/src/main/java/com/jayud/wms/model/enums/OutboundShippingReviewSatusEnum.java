package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/4/11 13:54
 * @description: 出库-发运复核状态
 */
@Getter
@AllArgsConstructor
public enum OutboundShippingReviewSatusEnum {

    UNREVIEW(1,"未复核"),
    REVIEWING(2,"复核中"),
    REVIEWED(3,"已复核");



    /**
     * 状态
     */
    private Integer status;

    /**
     * 描述
     */
    private String desc;

}
