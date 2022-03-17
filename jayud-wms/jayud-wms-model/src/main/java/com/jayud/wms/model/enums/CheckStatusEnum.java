package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * "盘点状态(1未盘点、2部分盘点、3已盘点)"
 */
@Getter
@AllArgsConstructor
public enum CheckStatusEnum {


    CHECKSTATUS_1(1,"未盘点"),
    CHECKSTATUS_2(2,"部分盘点"),
    CHECKSTATUS_3(3,"已盘点"),
    ;

    /**
     * 类型
     */
    private Integer typeCode;

    /**
     * 描述
     */
    private String typeDesc;

}
