package com.jayud.common.aop;

import cn.hutool.core.util.TypeUtil;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.aop.annotations.DynamicHead;
import io.swagger.annotations.ApiModelProperty;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@Aspect
@Component
public class ControllerAspect {

    @Pointcut("@annotation(com.jayud.common.aop.annotations.DynamicHead)")
    public void dynamicHeadPointCut() {
    }

    ;

    @AfterReturning(value = "@annotation(com.jayud.common.aop.annotations.DynamicHead)", returning = "keys")
    public void afterReturning(JoinPoint joinPoint, Object keys) throws ClassNotFoundException {
        if (keys instanceof CommonResult) {
            CommonResult resultVO = (CommonResult) keys;
            Object data = resultVO.getData();
            if (resultVO.getData() instanceof CommonPageResult) {
                List list = ((CommonPageResult) data).getList();
                data = list.size() == 0 ? null : list;
            }
            data = data;

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            Field[] fields = null;
            DynamicHead dynamicHead = method.getAnnotation(DynamicHead.class);
            if (data == null) {
                if (dynamicHead.clz() != Class.class) {
                    fields = dynamicHead.clz().getDeclaredFields();
                } else {
                    Type type = TypeUtil.getReturnType(method);
                    Type returnType = TypeUtil.getTypeArgument(type);
                    returnType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
                    fields = Class.forName(returnType.getTypeName()).getDeclaredFields();
                }
            } else {
                fields = data.getClass().getDeclaredFields();
            }

            List<Map<String, Object>> heads = new ArrayList<>();
            for (Field field : fields) {

                if (field.isAnnotationPresent(ApiModelProperty.class)) {
                    Map<String, Object> head = new HashMap<>();
                    ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                    head.put("name", field.getName());
                    head.put("viewName", annotation.value());
                    head.put("default", annotation.required());
                    heads.add(head);
                }
            }


            Map<String, Object> result = new HashMap<>();
            result.put(dynamicHead.headKey(), heads);
            result.put(dynamicHead.dataKey(), data);
            resultVO.setData(result);
        }
    }


}
