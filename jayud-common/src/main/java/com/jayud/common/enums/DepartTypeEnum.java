package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2022/2/25 11:44
 * @description: 部门类型
 */
@Getter
@AllArgsConstructor
public enum DepartTypeEnum {

    GROUP("1","集团"),
    COMPANY("2","公司"),
    DEPART("3","部门");

    /**
     * 部门类型
     */
    private String departType;

    /**
     * 描述
     */
    private String desc;

}
