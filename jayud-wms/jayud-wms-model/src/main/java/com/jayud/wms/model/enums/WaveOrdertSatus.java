package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2021/12/23 13:50
 * @description: 波次单--状态枚举
 */
@Getter
@AllArgsConstructor
public enum WaveOrdertSatus {

    UNASSIGNED(1,"未分配"),

    HANDING(2,"处理中"),

    ASSIGNED(3,"已分配"),

    OUT_STOCK(4,"缺货中"),

    ISSUED(5,"已出库"),

    CANCEL(6,"已撤销");


    /**
     * 类型
     */
    private final Integer type;

    /**
     * 信息
     */
    private final String msg;
}
