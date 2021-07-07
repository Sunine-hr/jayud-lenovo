package com.jayud.common.config;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.Field;

import static springfox.documentation.schema.Annotations.findPropertyAnnotation;
import static springfox.documentation.swagger.schema.ApiModelProperties.findApiModePropertyAnnotation;

/**
 * swagger接口模型排序和java类定义顺序保持一致
 *
 * @Auther dywen
 * @Date 2020/6/16
 */
@Component
@Slf4j
public class SwaggerModelSortInit implements ModelPropertyBuilderPlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        Optional<BeanPropertyDefinition> beanPropertyDefinitionOpt = context.getBeanPropertyDefinition();

        Optional<ApiModelProperty> annotation = Optional.absent();

        if (context.getAnnotatedElement().isPresent()) {

            annotation = annotation.or(findApiModePropertyAnnotation(context.getAnnotatedElement().get()));

        }

        if (context.getBeanPropertyDefinition().isPresent()) {

            annotation = annotation.or(findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class));

        }

        if (beanPropertyDefinitionOpt.isPresent()) {

            BeanPropertyDefinition beanPropertyDefinition = beanPropertyDefinitionOpt.get();

            if (annotation.isPresent() && annotation.get().position() != 0) {

                return;

            }

            AnnotatedField field = beanPropertyDefinition.getField();

            Class<?> clazz = field.getDeclaringClass();

            Field[] declaredFields = clazz.getDeclaredFields();

            Field declaredField;

            try {

                declaredField = clazz.getDeclaredField(field.getName());

            } catch (NoSuchFieldException | SecurityException e) {

                log.error("", e);

                return;

            }

            int indexOf = ArrayUtils.indexOf(declaredFields, declaredField);

            if (indexOf != -1) {

                context.getBuilder().position(indexOf);

            }

        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {

        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
