package com.jayud.common.aop.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface FieldLabel {
    //中文备注
    String name() default "";

    /**
     * 字段中文映射关系对应关系
     * 格式   ：   映射前字段值:映射后字段值（多个用逗号","隔开）
     * 例    ：   0:初始化,1:启用,2:禁用
     *
     * @return
     */
    String mappingString() default "";
}
