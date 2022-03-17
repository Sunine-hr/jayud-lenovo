package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/1/17 13:47
 * @description:    分配策略类型枚举
 */
@Getter
@AllArgsConstructor
public enum AllocationStrategyTypeEnum {

    DEFAULT(1,"默认"),
    CUSTOMIZE(2,"自定义");


    /**
     * 类型
     */
    private int type;

    /**
     * 描述
     */
    private String desc;


}
