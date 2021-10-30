package com.jayud.common.utils;

import com.github.dadiyang.equator.Equator;
import com.github.dadiyang.equator.FieldInfo;
import com.github.dadiyang.equator.GetterBaseEquator;
import com.jayud.common.aop.annotations.FieldLabel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.core.ClassInfo;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 对比操作
 */
@Slf4j
public class ComparisonOptUtil {
    // 由于SimpleDateFormat不是线程安全的，所以使用ThreadLocal来存储
//    private static ThreadLocal<DateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    //缓存注解
    private static HashMap<String, Map<String, FieldLabel>> classAnnotationMap = new HashMap<>();

    /**
     * 生成修改操作详情
     *
     * @param first  修改前的对象
     * @param second 修改后的对象
     * @return
     */
    public static String getDifferentValuesStr(Object first, Object second) {
        StringBuilder sb = new StringBuilder();
        if (first == null || second == null) {
            return null;
        }
        Equator equator = new GetterBaseEquator();
        List<FieldInfo> fieldInfos = equator.getDiffFields(first, second);
        if (CollectionUtils.isEmpty(fieldInfos)) {
            return null;
        }
        Class<?> clazz = second.getClass();
        for (FieldInfo fieldInfo : fieldInfos) {
            FieldLabel annotation = getAnnotation(clazz, fieldInfo.getFieldName());
            if (annotation == null) {
                continue;
            }
            Object firstVal = fieldInfo.getFirstVal();
            Object secondVal = fieldInfo.getSecondVal();
            // 处理时间格式
//            if ("java.util.Date".equals(fieldInfo.getFirstFieldType().getName()) && firstVal != null) {
//                firstVal = dateFormatThreadLocal.get().format(firstVal);
//            }
//            if ("java.util.Date".equals(fieldInfo.getSecondFieldType().getName()) && secondVal != null) {
//                secondVal = dateFormatThreadLocal.get().format(secondVal);
//            }
            //处理映射关系
            if (!StringUtils.isEmpty(annotation.mappingString())) {
                Map<String, String> mappingMap = mappingStringToMap(annotation.mappingString());
                String firstMappingVal = mappingMap.get(firstVal.toString());
                if (!StringUtils.isEmpty(firstMappingVal)) {
                    firstVal = String.format("%s", firstMappingVal);
                }
                String secondMappingVal = mappingMap.get(secondVal.toString());
                if (!StringUtils.isEmpty(secondMappingVal)) {
                    secondVal = String.format("%s", secondMappingVal);
                }
            }
            if (firstVal==null){
                sb.append(String.format("%s :  %s ", annotation.name(), secondVal));
            }else if (secondVal==null){
                sb.append(String.format("%s :  %s ", annotation.name(), firstVal));
            }else {
                sb.append(String.format("%s :  %s  →  %s", annotation.name(), firstVal, secondVal));
            }

            sb.append(",");
        }
        return sb.toString();
    }

    private static Map<String, String> mappingStringToMap(String mappingString) {
        return Arrays.stream(mappingString.trim().split(",|，")).map(e -> e.trim().split(":|："))
                .collect(Collectors.toMap(e -> e.length > 0 ? e[0].trim() : "", e -> e.length > 1 ? e[1].trim() : ""));
    }

    private static FieldLabel getAnnotation(Class<?> clazz, String annotationName) {
        String className = clazz.getName();
        Map<String, FieldLabel> labelMap = classAnnotationMap.get(className);
        if (labelMap == null) {
            labelMap = new HashMap<>();
            classAnnotationMap.put(className, labelMap);
        }
        //从对象的字段中获取注解
        FieldLabel fieldLabel = labelMap.get(annotationName);
        if (fieldLabel == null) {
            try {
                fieldLabel = clazz.getDeclaredField(annotationName).getAnnotation(FieldLabel.class);
                if (fieldLabel != null) {
                    labelMap.put(annotationName, fieldLabel);
                }
            } catch (NoSuchFieldException e) {
                log.warn("读取注解信息失败", e);
            }
        }

        return fieldLabel;
    }
}
