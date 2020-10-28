package com.jayud.common.utils.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColProperties {
    /**
     * 字段名称
     *
     * @return
     */
    String name() default "";

    /**
     * 对应的列数（1=第一列）
     *
     * @return
     */
    int col() default 0;

    /**
     * 输出时使用：
     * <br>在特定情况下读取数据时
     * <br>
     *
     * @return
     */
    boolean forOutput() default false;

    /**
     * 本列是否存在合并单元格
     *
     * @return
     */
    boolean mayMerged() default false;


}
