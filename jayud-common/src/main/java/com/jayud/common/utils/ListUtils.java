package com.jayud.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ciro
 * @date 2021/12/14 10:06
 * @description: list工具类
 */
public class ListUtils {

    /**
     * @description 获取不同数据
     * @author  ciro
     * @date   2021/12/14 10:07
     * @param: main
     * @param: diff
     * @return: java.util.List<java.lang.String>
     **/
    public static List<String> getDiff(List<String> main,List<String> diff){
        if (diff.isEmpty()){
            return null;
        }
        if (main.isEmpty()){
            return diff;
        }

        List<String> resList = new ArrayList<>();
        for (String diffValue : diff){
            if (StringUtils.isNotBlank(diffValue)&&!main.contains(diffValue)){
                resList.add(diffValue);
            }
        }
        return resList;
    }

    /**
     * @description 获取相同数据
     * @author  ciro
     * @date   2021/12/22 15:30
     * @param: main
     * @param: diff
     * @return: java.util.List<java.lang.String>
     **/
    public static List<String> getSame(List<String> main,List<String> diff){
        if (diff.isEmpty()){
            return null;
        }
        if (main.isEmpty()){
            return diff;
        }

        List<String> resList = new ArrayList<>();
        for (String diffValue : diff){
            if (StringUtils.isNotBlank(diffValue)&&main.contains(diffValue)){
                resList.add(diffValue);
            }
        }
        return resList;
    }

    /**
     * @description 去除相同数据
     * @author  ciro
     * @date   2021/12/29 14:49
     * @param: mainList
     * @return: java.util.List<java.lang.String>
     **/
    public static List<String> removeSame(List<String> mainList){
        List<String> reList = new ArrayList<>();
        if (mainList == null){
            return reList;
        }
        mainList.forEach(x->{
            if (!reList.contains(x)){
                reList.add(x);
            }
        });
        return reList;
    }
}
