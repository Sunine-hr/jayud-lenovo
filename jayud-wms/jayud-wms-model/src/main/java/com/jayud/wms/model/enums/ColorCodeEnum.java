package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ColorCodeEnum {

    WHITE("white","白色"),
    ORANGE("orange","橙色"),
    GRAY("gray","灰色"),
    GREEN("green","绿色"),
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
