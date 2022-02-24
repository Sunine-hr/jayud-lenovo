package com.jayud.common.aop.annotations;

import com.jayud.common.enums.SysLogTypeEnum;

import java.lang.annotation.*;

/**
 * @author ciro
 * @date 2021/12/13 16:47
 * @description: 系统日志注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLog {
    /**
     * 日志内容
     *
     * @return
     */
    String value() default "";

    /**
     * 日志类型：1-普通日志，2-登录志
     *
     * @return
     */
    SysLogTypeEnum logType() default SysLogTypeEnum.COMMON;
}
