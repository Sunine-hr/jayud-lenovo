package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShelfMoveTaskEnum {

    MTC01("MTC01","货架至工作台-收货"),
    MTC02("MTC02","工作台至货架-上架"),
    MTC03("MTC03","货架至工作台-下架"),
    MTC04("MTC04","工作台至货架-下架"),
    MTC05("MTC05","货架至工作台-盘点"),
    MTC06("MTC06","工作台至货架-盘点"),
    ;

    /**
     * 类型
     */
    private String typeCode;

    /**
     * 描述
     */
    private String typeDesc;

}
