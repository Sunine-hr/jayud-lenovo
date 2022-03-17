package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/3/10 14:35
 * @description: 拣货下架状态枚举
 */
@Getter
@AllArgsConstructor
public enum WarehouseLocationStatusEnum {

    IRRELEVANT(0,"无关"),

    WAIT(1,"待拣货下架"),

    FINISHED(3,"拣货下架完成");

    /**
     * 状态
     */
    Integer status;

    /**
     * 描述
     */
    String desc;
}
