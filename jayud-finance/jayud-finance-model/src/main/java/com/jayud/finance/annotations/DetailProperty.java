package com.jayud.finance.annotations;


import com.jayud.finance.enums.InvoiceFormTypeEnum;

import java.lang.annotation.*;

/**
 * 标记属性为表体属性
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DetailProperty {
    /**
     * 对应的json字段名
     *
     * @return
     */
    String name() default "";

    /**
     * 类型
     * <br>主 应收/应付 单（main）
     * <br>其他 应收/应付 单(other）
     *
     * @return
     */
    InvoiceFormTypeEnum type() default InvoiceFormTypeEnum.MAIN;

    /**
     * 是否需要包装
     *
     * @return
     */
    boolean wrap() default false;
    /**
     * 如果类型选用both，且主应收应付和其他应收应付对应字段不一致，用此指定其他应收应付的字段名
     * @return
     */
    String otherName() default "";

}
