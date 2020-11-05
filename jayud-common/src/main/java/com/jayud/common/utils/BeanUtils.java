package com.jayud.common.utils;

import com.jayud.common.func.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体工具类
 */
@Slf4j
public class BeanUtils {

    /**
     * 存储对象属性
     */
    private static Map<Class, SerializedLambda> CLASS_LAMDBA_CACHE = new ConcurrentHashMap<>();


    /**
     * 转换方法引用为属性名
     */
    public static <T> String convertToFieldName(SFunction<T> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        //获取方法名
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if (methodName.startsWith("get")) {
            prefix = "get";
        } else if (methodName.startsWith("is")) {
            prefix = "is";
        }
        if (prefix == null) {
            log.error("无效的getter方法: " + methodName);
        }
        // 截取get/is之后的字符串并转换首字母为小写
        return StringUtils.toLowerCaseFirstOne(methodName.replace(prefix, ""));
    }

    public static <T> String[] convertToFieldName(boolean isTableField, SFunction<T>... fns) {
        String[] fields = new String[fns.length];
        for (int i = 0; i < fns.length; i++) {
            String str = convertToFieldName(fns[i]);
            if (isTableField) {
                str = StringUtils.humpToUnderline(str);
            }
            fields[i] = str;

        }
        return fields;
    }

    /**
     * 获取方法（关键在于这个方法）
     */
    private static <T> SerializedLambda getSerializedLambda(Serializable fn) {
        SerializedLambda lambda = CLASS_LAMDBA_CACHE.get(fn.getClass());
        //先检查缓存中是已存在
        if (lambda == null) {
            try {
                //提取SerializedLambda并缓存
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(true);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMDBA_CACHE.put(fn.getClass(), lambda);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lambda;
    }
}
