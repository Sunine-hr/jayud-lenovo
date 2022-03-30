package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ciro
 * @date 2021/12/13 16:48
 * @description: 系统日志枚举
 */
@Getter
@AllArgsConstructor
public enum SysLogTypeEnum {
    /**
     * 操作
     */
    COMMON(1, "操作日志"),
    /**
     * 登录
     */
    LOGIN(2, "登录日志");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 信息
     */
    private final String msg;
}
