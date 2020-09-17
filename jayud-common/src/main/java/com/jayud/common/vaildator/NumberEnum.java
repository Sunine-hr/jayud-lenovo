package com.jayud.common.vaildator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于校验入参中需要限定数字枚举的请求参数
 * <br>用于弥补  {@link Pattern} 无法校验入参对应属性为Integer的不足
 * @author william
 * @description
 * @Date: 2020-09-16 13:24
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumberEnumValidator.class)
public @interface NumberEnum {
    /**
     * 格式："1,2,3,4,5,6"
     * @return
     */
    String enums() default "";
    String message() default "数字校验失败";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
