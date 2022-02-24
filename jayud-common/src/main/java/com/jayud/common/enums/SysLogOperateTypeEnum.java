package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ciro
 * @date 2021/12/13 17:00
 * @description: 系统日志枚举
 */
@Getter
@AllArgsConstructor
public enum SysLogOperateTypeEnum {
    /**
     * 查询
     */
    LIST(SysLogTypeEnum.COMMON, 1, "list"),
    /**
     * 新增
     */
    ADD(SysLogTypeEnum.COMMON, 2, "add"),
    /**
     * 跟新
     */
    UPDATE(SysLogTypeEnum.COMMON, 3, "update"),
    /**
     * 删除
     */
    DEL(SysLogTypeEnum.COMMON, 4, "del"),
    /**
     * 导入
     */
    IMPORT(SysLogTypeEnum.COMMON, 5, "import"),
    /**
     * 导出
     */
    EXPORT(SysLogTypeEnum.COMMON, 6, "export"),
    /**
     * 登录
     */
    LOGIN(SysLogTypeEnum.LOGIN, 1, "login"),
    /**
     * 登出
     */
    LOGOUT(SysLogTypeEnum.LOGIN, 2, "logout");

    /**
     * 日志类型
     */
    private final SysLogTypeEnum sysLogType;

    /**
     * 操作类型
     */
    private final Integer type;

    /**
     * 标志信息
     */
    private final String mark;

    /**
     * 获取操作类型
     *
     * @param sysLogEnum 系统日志类型
     * @param methodName 方法名
     * @return 操作类型
     */
    public static Integer getOperateType(SysLogTypeEnum sysLogEnum, String methodName) {
        switch (sysLogEnum) {
            case COMMON:
                if (methodName.startsWith(LIST.getMark()) || methodName.endsWith(LIST.getMark())) {
                    return LIST.getType();
                }
                if (methodName.startsWith(ADD.getMark()) || methodName.endsWith(ADD.getMark())) {
                    return ADD.getType();
                }
                if (methodName.startsWith(UPDATE.getMark()) || methodName.endsWith(UPDATE.getMark())) {
                    return UPDATE.getType();
                }
                if (methodName.startsWith(DEL.getMark()) || methodName.endsWith(DEL.getMark())) {
                    return DEL.getType();
                }
                if (methodName.startsWith(IMPORT.getMark()) || methodName.endsWith(IMPORT.getMark())) {
                    return IMPORT.getType();
                }
                if (methodName.startsWith(EXPORT.getMark()) || methodName.endsWith(EXPORT.getMark())) {
                    return EXPORT.getType();
                }
            case LOGIN:
                if (methodName.startsWith(LOGIN.getMark()) || StringUtils.endsWithIgnoreCase(methodName, LOGIN.getMark())) {
                    return LOGIN.getType();
                }

                if (StringUtils.startsWith(methodName, LOGOUT.getMark()) || StringUtils.endsWithIgnoreCase(methodName, LOGOUT.getMark())) {
                    return LOGOUT.getType();
                }
            default:
                return 0;
        }
    }
}
