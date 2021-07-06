package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 天数标志
 */
@Getter
@AllArgsConstructor
public enum DayFlagEnum {

    ETD("ETD", "开船日期"),
    ETA("ETA", "到港日期"),
    RUL("RUL", "跨运单、提单任务"),
    STE("STE", "未激活")
    ;

    private String code;
    private String describe;
}
