package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2021/12/29 9:42
 * @description: 下架单状态
 */
@Getter
@AllArgsConstructor
public enum OffshelfTaskStatusEnum {

    WAIT_PACKING(1, "待拣货下架"),
    PACKING(2, "拣货下架中"),
    FINISH_PACKING(3, "拣货下架完成");

    /**
     * 状态
     */
    private int status;

    /**
     * 描述
     */
    private String desc;
}
