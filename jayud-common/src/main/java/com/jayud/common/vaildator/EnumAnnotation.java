package com.jayud.common.vaildator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValidtor.class})
@Documented
public @interface EnumAnnotation {

    String message() default "";

    Class<?>[] target() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
