package com.jayud.common.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Utilities {

    /**
     * 组装头部信息
     */
    public static List<Map<String, Object>> assembleEntityHead(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Map<String, Object>> heads = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ApiModelProperty.class)) {
                Map<String, Object> head = new HashMap<>(3);
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                if (annotation.example().length() > 0 ||
                        annotation.required()) {
                    head.put("name", field.getName());
                    head.put("viewName", annotation.value());
                    head.put("default", annotation.required());
                    heads.add(head);
                }
            }
        }
        return heads;
    }


    /**
     * Object转实体类
     */
    public static <T> List<T> obj2List(Object obj, Class<T> clazz) {
        return JSONUtil.toList(new JSONArray(obj), clazz);
    }
}
