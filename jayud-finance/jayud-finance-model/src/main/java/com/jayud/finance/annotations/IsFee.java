package com.jayud.finance.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记应收实体中数据是否为费用项
 * @author william
 * @description
 * @Date: 2020-09-23 15:46
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsFee {

}
