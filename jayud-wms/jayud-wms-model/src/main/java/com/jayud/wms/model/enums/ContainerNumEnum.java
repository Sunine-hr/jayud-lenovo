package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认容器id
 */
@Getter
@AllArgsConstructor
public enum ContainerNumEnum {

    /**
     * 默认容器id
     */
    DEFAULT(0L,"0000")
    ;


    /**
     * 容器id
     */
    private Long containerId;

    /**
     * 容器号
     */
    private String containerCode;

}
