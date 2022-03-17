package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/1/17 11:14
 * @description: 分配策略详情类型枚举
 */
@Getter
@AllArgsConstructor
public enum AllocationStrategyDetailTypeEnum {

    LINE_SWQUENCE(1,"线路排序"),
    EMPTY_LOCATION(2,"清空库位");


    /**
     * 类型
     */
    private int type;

    /**
     * 描述
     */
    private String desc;

}
