package com.jayud.common.vaildator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

public class EnumValidtor implements ConstraintValidator<EnumAnnotation,Object> {

    Class[] cls;

    @Override
    public void initialize(EnumAnnotation constraintAnnotation) {
        cls = constraintAnnotation.target();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if(cls.length>0){
            for(Class cl : cls){
                try {
                    if(cl.isEnum() && value!=null){
                        //枚举类验证
                        Object[] objs = cl.getEnumConstants();
                        Method method = cl.getMethod("getCode");
                        for (Object obj : objs) {
                            Object code = method.invoke(obj);
                            if(value.toString().equals(code.toString())){
                                return true;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
}
