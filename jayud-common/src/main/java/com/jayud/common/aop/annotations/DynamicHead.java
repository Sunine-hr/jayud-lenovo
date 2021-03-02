package com.jayud.common.aop.annotations;

import java.lang.annotation.*;

/**
 * @author L
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DynamicHead {
    String headKey() default "head";

    String dataKey() default "data";

    Class clz() default Class.class;
}
