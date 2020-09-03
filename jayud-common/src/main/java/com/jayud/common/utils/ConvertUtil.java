package com.jayud.common.utils;

import cn.hutool.core.bean.BeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数类型转换工具类
 */
public class ConvertUtil {

    /**
     * 参数转换
     * @param source    原始对象
     * @param clz        转换后的对象类型
     * @return
     */
    public final static <S,T> T convert(S source,Class<T> clz){
        T target = null;
        try {
            target = clz.newInstance();
            BeanUtil.copyProperties(source,target);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 参数转换
     * @param sourceList    原始对象集合
     * @param clz        转换后的对象类型
     * @return
     */
    public static <S,T> List<T> convertList(List<S> sourceList, Class<T> clz){
        return sourceList.stream().map(e->{
            return convert(e,clz);
        }).collect(Collectors.toList());
    }
}
