package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 货架查询类型
 */
@Getter
@AllArgsConstructor
public enum ShelfQueryTypeEnum {

    INPUT("input","入库"),
    OUTPUT("output","出库"),
    CHECK("check","盘点");

    /**
     * 类型
     */
    private String typeCode;

    /**
     * 描述
     */
    private String typeDesc;
}
