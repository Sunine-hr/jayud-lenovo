package com.jayud.common.vaildator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author william
 * @description
 * @Date: 2020-09-16 13:29
 */
public class NumberEnumValidator implements ConstraintValidator<NumberEnum, Integer> {
    private NumberEnum numberEnum;

    @Override
    public void initialize(NumberEnum constraint) {
        this.numberEnum = constraint;
    }

    @Override
    public boolean isValid(Integer obj, ConstraintValidatorContext context) {
        //如果enum为空则接受任何形式的数字
        String enums = numberEnum.enums();
        if (!Objects.equals("", enums)) {
            List<String> integerStrings = Arrays.asList(enums.split(","));
            try {
                List<Integer> integers = integerStrings.stream().map(e -> {
                    return Integer.parseInt(e);
                }).collect(Collectors.toList());
                if (integers.contains(obj)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
            return false;
        }
        return true;
    }
}
