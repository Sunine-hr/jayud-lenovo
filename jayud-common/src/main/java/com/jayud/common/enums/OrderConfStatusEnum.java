package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * nanjing mall 配载单状态枚举
 * 南京商城
 */
@Getter
@AllArgsConstructor
public enum OrderConfStatusEnum {

    PREPARE("0", "准备"),
    ENABLE("10", "启用"),
    START_AUTOSTOW("20", "开始配载"),
    TRANSIT("30", "转运中"),
    FINISH("40", "完成"),
    CANCEL("-1", "取消");

    private String code;
    private String name;

}
