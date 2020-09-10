package com.jayud.po;

import com.jayud.enums.FormIDEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * K3 基本数据返回字段 配置
 * @author bocong.zheng
 */
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface K3Entity {

    /**
     * k3 基本数据实体返回字段
     * @return
     */
    String[] value() default  {};

    /**
     * k3 实体 formId 枚举
     * @return
     */
    FormIDEnum formId() default FormIDEnum.PAYABLE;

    /**
     * k3 实体 数据 名称字段名，用于查询语句拼接
     * @return
     */
    String fName() default "FName";
}
