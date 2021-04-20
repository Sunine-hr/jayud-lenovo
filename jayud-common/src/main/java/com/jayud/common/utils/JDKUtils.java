package com.jayud.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    public static <T, K, U extends Comparable<? super U>> List<T> getGroupByLastData(List<T> list,
                                                                                     Function<? super T, ? extends U> orderBy,
                                                                                     Function<? super T, ? extends K> groupBy) {
        List<T> newList = new ArrayList<>();
//        Stream<T> stream = list.stream();
//        if (orderBy != null) {
//            //按照排序 reversed()是反转,升序变为降序,也就是说把最新的数据放到list首
//            stream.sorted(Comparator.comparing(orderBy).reversed());
//        }
//        //分组
//        Map<String, List<T>> map = (Map<String, List<T>>) stream.collect(Collectors.groupingBy(groupBy));
        Map<String, List<T>> map =
                //按照排序 reversed()是反转,升序变为降序,也就是说把最新的数据放到list首
                (Map<String, List<T>>) list.stream().sorted(Comparator.comparing(orderBy).reversed())
                        // 正常是用：：符号就可以 但是我这个对象比较特殊有三层的属性所以这么写了
                        .collect(Collectors.groupingBy(groupBy));

        for (Map.Entry<String, List<T>> entry : map.entrySet()) {
            if (CollectionUtils.isNotEmpty(entry.getValue())) {
                //取list首并放入到新list中
                newList.add(entry.getValue().get(0));
            }
        }
        return newList;
    }


}
