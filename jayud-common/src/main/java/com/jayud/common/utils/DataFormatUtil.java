package com.jayud.common.utils;


import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 86158
 */
public class DataFormatUtil {
    /**
     * 汉字转换类
     *
     * @param chineseNumber
     * @return
     */
    public static String chineseNumber2Int(String chineseNumber) {
        String result = "";
        String temp = "";//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            String c = chineseNumber.charAt(i) + "";
            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c.trim().equals(cnArr[j] + "".trim())) {
                    temp = j + "";
                    break;
                } else {
                    temp = "";
                }
            }
            result += temp;
        }
        return result;
    }

    /**
     * 日期转换
     *
     * @param str
     * @return
     */
    public static String dateFormat(String str) {
        //包含出票日期,截取日期
        //中文日期转换
        //截取年
        String year = chineseNumber2Int(str.substring(0, str.indexOf("年"))) + "";
        //截取月
        String month = chineseNumber2Int(str.substring(str.indexOf("年") + 1, str.indexOf("月"))) + "";
        if (StringUtils.isNotBlank(month)) {
            //大于12替换0
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            } else {
                month = month.replace("0", "");
            }
        } else {
            //月份为空
            return "false";
        }
        //截取日
        String day = chineseNumber2Int(str.substring(str.indexOf("月") + 1, str.indexOf("日"))) + "";
        if (StringUtils.isNotBlank(day) ) {
            if(Integer.parseInt(day) > 31){
                //大于31天替换10
                day = day.replace("10", "");
            }
        } else {
            return "false";
        }
        if (Integer.parseInt(day) < 10) {
            day = "0" + day;
        }
        //日期转换
        return year + "-" + month + "-" + day;
    }

    /**
     * 將中文替換為空
     *
     * @param s
     * @return
     */
    public static String replaceChineseToNULL(String s) {
        String reg = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);
        Matcher m = pat.matcher(s);
        return m.replaceAll("");
    }

    public static String dateParse(String dateTimeString) {
        try {
            if(dateTimeString.indexOf(":")!=-1){
                dateTimeString=dateTimeString.replace(":","");
            }
            if(dateTimeString.indexOf("/")!=-1){
                dateTimeString=dateTimeString.replace("/","-");
            }
            if(dateTimeString.indexOf("-")==-1) {
                //没有-分割字符串
                String year = dateTimeString.substring(0,4);
                String mon = dateTimeString.substring(4,6);
                String day = dateTimeString.substring(6,8);
                return year+"-"+mon+"-"+day;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTimeString;
    }

    /**
     * 数据组合
     *
     * @param str
     * @return
     */
    public static String getJEDate(String str) {
        List<String> datas = new ArrayList<String>(10);
        datas.add("壹分");
        datas.add("贰分");
        datas.add("叁分");
        datas.add("肆分");
        datas.add("伍分");
        datas.add("陆分");
        datas.add("柒分");
        datas.add("捌分");
        datas.add("玖分");
        datas.add("整");
        datas.add("壹角");
        datas.add("贰角");
        datas.add("叁角");
        datas.add("肆角");
        datas.add("伍角");
        datas.add("陆角");
        datas.add("柒角");
        datas.add("捌角");
        datas.add("玖角");
        //是否包含对应字符
        for (int i = 0; i < datas.size(); i++) {
            String st = datas.get(i);
            if (str.indexOf(st) != -1) {
                //如果整数特别处理
                if (st.equals("整")) {
                    return method(str);
                } else {
                    //包含对应字符串，字符转数字
                    return chineseNumber2Int(str);
                }
            }
        }
        return null;
    }

    /**
     * 查找第一次出現字符
     */

    public static String method(String str) {
        String msg = "";
        //过滤特别字符
        for (int i = 0; i < str.length(); i++) {
            String c = str.charAt(i) + "";
            if (c.equals("壹")) {
                msg += "壹";
            } else if (c.equals("贰")) {
                msg += "贰";
            } else if (c.equals("叁")) {
                msg += "叁";
            } else if (c.equals("肆")) {
                msg += "肆";
            } else if (c.equals("伍")) {
                msg += "伍";
            } else if (c.equals("陆")) {
                msg += "陆";
            } else if (c.equals("柒")) {
                msg +="柒";
            } else if (c.equals("捌")) {
                msg += "捌";
            } else if (c.equals("玖")) {
                msg += "玖";
            } else if (c.equals("拾")) {
                msg += "拾";
            } else if (c.equals("佰")) {
                msg += "佰";
            } else if (c.equals("仟")) {
                msg += "仟";
            } else if (c.equals("万")) {
                msg += "万";
            } else if (c.equals("亿")) {
                msg += "亿";
            }
        }
        String mon = MoneyUtil.rmbBigToSmall(msg);
        if (str.lastIndexOf("整") != -1) {
            mon += "00";
        }
        return mon;
    }

}
