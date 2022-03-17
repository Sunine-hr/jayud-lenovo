package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 明细盘点状态(1未盘点、2已盘点、3已过账)
 */
@Getter
@AllArgsConstructor
public enum CheckDetailStatusEnum {

    CHECKSTATUS_1(1,"未盘点"),
    CHECKSTATUS_2(2,"已盘点"),
    CHECKSTATUS_3(3,"已过账"),
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
