package com.jayud.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * jdk实用工具
 */
@Component
public class JDKUtils {


    /**
     * jdk8
     * 获取分组最新的`
     */
    public static <T, K, U extends Comparable<? super U>> Map<String, List<T>> getGroupMapByLastData(List<T> list,
                                                                                                   Function<? super T, ? extends U> orderBy,
                                                                                                   Function<? super T, ? extends K> groupBy) {
        Map<String, List<T>> map =
                //按照排序 reversed()是反转,升序变为降序,也就是说把最新的数据放到list首
                (Map<String, List<T>>) list.stream().sorted(Comparator.comparing(orderBy).reversed())
                        // 正常是用：：符号就可以 但是我这个对象比较特殊有三层的属性所以这么写了
                        .collect(Collectors.groupingBy(groupBy));
        return map;
    }


    /**
     * jdk8
     * 获取分组最新的`
     */
    public static <T, K, U extends Comparable<? super U>> List<T> getGroupByLastData(List<T> list,
                                                                                     Function<? super T, ? extends U> orderBy,
                                                                                     Function<? super T, ? extends K> groupBy) {
        List<T> newList = new ArrayList<>();
        Map<String, List<T>> map = getGroupMapByLastData(list, orderBy, groupBy);
        for (Map.Entry<String, List<T>> entry : map.entrySet()) {
            if (CollectionUtils.isNotEmpty(entry.getValue())) {
                //取list首并放入到新list中
                newList.add(entry.getValue().get(0));
            }
        }
        return newList;
    }


    /**
     * 根据属性中某一个字段去重
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
