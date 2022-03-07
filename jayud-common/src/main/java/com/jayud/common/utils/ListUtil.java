package com.jayud.common.utils;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    /**
     * 获取list中存放的最后一个元素
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T getLastElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }


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
            if (org.apache.commons.lang3.StringUtils.isNotBlank(diffValue)&&!main.contains(diffValue)){
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

    /**
     * @description 判断两个list内容是否相同
     * @author  ciro
     * @date   2022/3/7 16:52
     * @param: mainList
     * @param: lastList
     * @return: boolean
     **/
    public static boolean checkIsSame(List<String> mainList,List<String> lastList){
        boolean isSame = true;
        if (CollUtil.isEmpty(mainList) || CollUtil.isEmpty(lastList)){
            isSame = false;
        }else {
            for (String id : mainList){
                if (!lastList.contains(id)){
                    isSame = false;
                    break;
                }
            }
        }
        return isSame;
    }


}
