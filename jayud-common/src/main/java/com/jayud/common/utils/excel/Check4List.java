package com.jayud.common.utils.excel;

import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 校验List中的重复行
 *
 * @author william
 * @description
 * @Date: 2020-07-29 18:20
 */
public class Check4List {
    /**
     * 校验列表中被标记的字段不为空且不重复
     *
     * @param list
     */
    public static void checkNullNDuplicate(@NonNull List<?> list) {
        String errorString = getNullOrDuplicateErrorString(list);
        if (!errorString.isEmpty()) {
            Asserts.fail(ResultEnum.PARAM_ERROR, errorString);
        }
    }

    /**
     * 校验列表中被标记的字段不为空且不重复，并返回检出的重复信息
     *
     * @param list
     * @return
     */
    public static String getNullOrDuplicateErrorString(@NonNull List<?> list) {
        if (list.isEmpty()) {
            return null;
        }
        List<String> colNames = new ArrayList<>();
        Class<?> aClass = list.get(0).getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Annotation[] declaredAnnotations = declaredField.getDeclaredAnnotations();
            for (Annotation declaredAnnotation : declaredAnnotations) {
                if (declaredAnnotation.annotationType() == NotDuplicate.class) {
                    colNames.add(declaredField.getName());
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String colName : colNames) {
            doDuplicateCheck(colName, list, sb);
        }
        return sb.toString();
    }




    /**
     * 查重：非空且不重复
     *
     * @param columnName
     * @param list
     * @param sb
     */
    private static void doDuplicateCheck(String columnName, List<?> list, StringBuilder sb) {
        try {
            Class<?> clz = list.get(0).getClass();
            //获取指定名字的属性的get方法
            Method getMethod = clz.getMethod("get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1));

            //处理
            List<Object> checkForDuplicate = list.stream().map(e -> {
                Object o = null;
                try {
                    o = getMethod.invoke(e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return o;
            }).collect(Collectors.toList());

            Map<Object, Object> unitMap = new HashMap<>();
            for (Integer i = 0; i < checkForDuplicate.size(); i++) {
                Object name = checkForDuplicate.get(i);
                if (Objects.isNull(name)) {
                    sb.append("第" + (i + 1) + "行的" + columnName + "不可为空，请检查; ");
                } else {
                    if (Objects.isNull(unitMap.get(name))) {
                        unitMap.put(name, i + 1);
                    } else {
                        sb.append("第" + (i + 1) + "行的" + columnName + " " + name + "与第" + unitMap.get(name) + "行重复，请检查; ");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
