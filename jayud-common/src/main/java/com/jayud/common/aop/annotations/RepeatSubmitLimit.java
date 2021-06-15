package com.jayud.common.aop.annotations;

import java.lang.annotation.*;

/**
 * 防重提交
 * @author L
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RepeatSubmitLimit {

    // 1000毫秒过期，1000ms内的重复请求会认为重复
    long expireTime() default 1000L;
}
