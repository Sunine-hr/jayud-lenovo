package com.jayud.common.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.*;

@Component
public class Utilities {

    /**
     * 组装头部信息
     */
    public static List<Map<String, Object>> assembleEntityHead(Class clazz, Boolean isParentFields) {
        Field[] fields = clazz.getDeclaredFields();
        ArrayList<Field> tmp = new ArrayList<Field>(Arrays.asList(fields));
        if (isParentFields) {
            Field[] parenFields = clazz.getSuperclass().getDeclaredFields();
            tmp.addAll(Arrays.asList(parenFields));
        }
        List<Map<String, Object>> heads = new ArrayList<>();
        for (Field field : tmp) {
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

    public static String printCheckCode(Class clazz) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class type = field.getType();
            String name = field.getName();
            if (!field.isAnnotationPresent(ApiModelProperty.class)) {
                continue;
            }
            String desc = field.getAnnotation(ApiModelProperty.class).value();
            if (String.class.isAssignableFrom(type)) {
                sb.append("if (StringUtils.isEmpty(" + name + ")){throw new JayudBizException(400,\"" + desc + "不能为空\"); }")
                        .append("\n");
            } else {
                sb.append("if (" + name + "==null){throw new JayudBizException(400,\"" + desc + "不能为空\"); }")
                        .append("\n");
            }
        }
        System.out.println(sb.toString());
        setClipboardString(sb.toString());
        return sb.toString();
    }


    public static String printFieldsInfo(Class clazz) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            if (!field.isAnnotationPresent(ApiModelProperty.class)) {
                continue;
            }
            String desc = field.getAnnotation(ApiModelProperty.class).value();
            sb.append(name).append("-").append(desc)
                    .append("\n");

        }
        System.out.println(sb.toString());
        setClipboardString(sb.toString());
        return sb.toString();
    }


    /**
     * 把文本设置到剪贴板（复制）
     */
    public static void setClipboardString(String text) {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
    }


    /**
     * 动态修改注解
     */
    public static Class dynamicUpdateAnnotations(Class clazz, List<String> names, Class annotation, Map annotationParam)
            throws NoSuchFieldException, IllegalAccessException {
        for (String name : names) {
            dynamicUpdateAnnotations(clazz, name, annotation, annotationParam);
        }
        return clazz;
    }

    /**
     * 动态修改注解
     */
    public static Class dynamicUpdateAnnotations(Class clazz, String name, Class annotation, Map annotationParam)
            throws NoSuchFieldException, IllegalAccessException {

        Field hField = clazz.getDeclaredField(name);
        hField.setAccessible(true);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(hField.getAnnotation(annotation));
        // 获取私有 memberValues 属性
        Field memberValuesField = invocationHandler.getClass().getDeclaredField("memberValues");
        memberValuesField.setAccessible(true);
        // 获取实例的属性map
        Map<String, Object> memberValuesValue = (Map<String, Object>) memberValuesField.get(invocationHandler);
        // 修改属性值
        memberValuesValue.putAll(annotationParam);
        return clazz;
    }
}
