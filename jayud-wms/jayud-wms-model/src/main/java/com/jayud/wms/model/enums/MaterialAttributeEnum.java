package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/1/12 10:11
 * @description: 物料-批属性
 */
@Getter
@AllArgsConstructor
public enum MaterialAttributeEnum {
    ONE(1, "批属性1"),
    TWO(2, "批属性2"),
    THREE(3, "批属性3"),
    FOUR(4, "批属性4"),
    FIVE(5, "批属性5"),
    SIX(6, "批属性6");

    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;
}
