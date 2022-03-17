package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/1/26 11:17
 * @description: 物料信息-周转方式枚举
 */
@Getter
@AllArgsConstructor
public enum MaterialTurnoverModeEnum {

    DESC(1,"先进先出"),
    ASC(2,"后进先出");


    private Integer type;

    private String desc;

}
