package com.jayud.common.utils;

import io.netty.util.internal.StringUtil;
import org.apache.http.util.TextUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 获取方法中指定注解的value值返回
     *
     * @param method               方法名
     * @param validationParamValue 注解的类名
     * @return
     */
    public static String getMethodAnnotationOne(Method method, String validationParamValue) {
        String retParam = null;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                String str = parameterAnnotations[i][j].toString();
                if (str.indexOf(validationParamValue) > 0) {
                    retParam = str.substring(str.indexOf("=") + 1, str.indexOf(")"));
                }
            }
        }
        return retParam;
    }

    /**
     * 给获取的企业打码
     *
     * @param companyName
     * @return
     */
    public static String getCompanyName(String companyName) {
        String prefix = null; //前缀
        String suffix = null; //后缀
        companyName = companyName.replaceAll("（", StringUtil.EMPTY_STRING).replaceAll("）", StringUtil.EMPTY_STRING);
        if (org.apache.commons.lang.StringUtils.isNotBlank(companyName)) {
            if (companyName.length() <= 6 && companyName.length() >= 2) {
                suffix = companyName.substring(companyName.length() - 2);
                if (!suffix.equals("公司")) {
                    prefix = companyName.substring(0, 1);
                    suffix = companyName.substring(companyName.length() - 1);
                }
            } else if (companyName.length() > 6) {
                String suffix6 = companyName.substring(companyName.length() - 6);
                String suffix4 = companyName.substring(companyName.length() - 4);
                String suffix2 = companyName.substring(companyName.length() - 2);
                String prefix3 = companyName.substring(0, 3);
                String prefix2 = companyName.substring(0, 2);
                String prefix1 = companyName.substring(0, 1);
                if ("股份有限公司".equals(suffix6)) {
                    suffix = companyName.substring(companyName.length() - 6);
                } else {
                    String substring = companyName.substring(companyName.length() - 4);
                    if ("有限公司".equals(suffix4)) {
                        suffix = substring;
                    } else if ("集团公司".equals(suffix4)) {
                        suffix = substring;
                    } else if ("集团".equals(suffix2)) {
                        suffix = companyName.substring(companyName.length() - 2);
                    } else if ("公司".equals(suffix2)) {
                        suffix = companyName.substring(companyName.length() - 2);
                    } else {
                        suffix = companyName.substring(companyName.length() - 2);
                    }
                }
                //前缀
                if ("中国".equals(prefix2)) {
                    prefix = companyName.substring(0, 3);
                } else if ("省".equals(prefix3.substring(prefix3.length() - 1)) || prefix3.substring(prefix3.length() - 1).equals("市")) {
                    prefix = companyName.substring(0, 4);
                } else if (prefix1.equals("中")) {
                    prefix = companyName.substring(0, 2);
                } else {
                    prefix = companyName.substring(0, 3);
                }
            }
            return prefix + "***" + suffix;
        } else {
            return "";
        }
    }

    /**
     * 用户身份证号码的打码隐藏加星号加*
     * <p>18位和非18位身份证处理均可成功处理</p>
     * <p>参数异常直接返回null</p>
     *
     * @param idCardNum 身份证号码
     * @param front     需要显示前几位
     * @param end       需要显示末几位
     * @return 处理完成的身份证
     */
    public static String idMask(String idCardNum, int front, int end) {
        //身份证不能为空
        if (TextUtils.isEmpty(idCardNum)) {
            return null;
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > idCardNum.length()) {
            return null;
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return null;
        }
        //计算*的数量
        int asteriskCount = idCardNum.length() - (front + end);
        StringBuilder asteriskStr = new StringBuilder();
        for (int i = 0; i < asteriskCount; i++) {
            asteriskStr.append("*");
        }
        String regex = "(\\w{" + front + "})(\\w+)(\\w{" + end + "})";
        return idCardNum.replaceAll(regex, "$1" + asteriskStr + "$3");
    }

    /**
     * 生成规定的n位随机不重复的数
     *
     * @param randomNum 随机数长度
     * @return
     */
    public static String loadNum(String head, int randomNum) {
        int roandm = 0;
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer stringBuffer = new StringBuffer("");
        Random r = new Random();
        for (int count = 0; count < randomNum; count++) {
            //生成10以内的随机整数
            roandm = Math.abs(r.nextInt(10));
            if (roandm >= 0 || roandm < str.length) {
                stringBuffer.append("" + str[roandm]);
            }
        }
        return head + "" + stringBuffer.toString();
    }

    public static String generate() {
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean[] flags = new boolean[letters.length];
        char[] chs = new char[6];
        for (int i = 0; i < chs.length; i++) {
            int index;
            do {
                index = (int) (Math.random() * (letters.length));
            }
            // 判断生成的字符是否重复
            while (flags[index]);
            chs[i] = letters[index];
            flags[index] = true;
        }
        return new StringBuilder().append(chs).toString();
    }

    /**
     * 将附件集合处理成字符串逗号拼接
     *
     * @param fileViewList
     * @return
     */
    public static String getFileStr(List<FileView> fileViewList) {
        String fileStr = "";
        if (fileViewList == null || fileViewList.size() == 0) {
            return fileStr;
        }
        StringBuilder sb = new StringBuilder();
        for (FileView fileView : fileViewList) {
            sb.append(fileView.getRelativePath()).append(",");
        }
        if (!"".equals(String.valueOf(sb))) {
            fileStr = sb.substring(0, sb.length() - 1);
        }
        return fileStr;
    }

    /**
     * 将附件集合处理成字符串逗号拼接
     *
     * @param fileViewList
     * @return
     */
    public static String getFileNameStr(List<FileView> fileViewList) {
        String fileNameStr = "";
        if (fileViewList == null || fileViewList.size() == 0) {
            return fileNameStr;
        }
        StringBuilder sb = new StringBuilder();
        for (FileView fileView : fileViewList) {
            sb.append(fileView.getFileName()).append(",");
        }
        if (!"".equals(String.valueOf(sb))) {
            fileNameStr = sb.substring(0, sb.length() - 1);
        }
        return fileNameStr;
    }

    /**
     * 把字符串解析成文件数组
     *
     * @param fileStr
     * @return
     */
    public static List<FileView> getFileViews(String fileStr, String fileNameStr, String prePath) {
        List<FileView> fileViews = new ArrayList<>();
        if (fileStr != null && !"".equals(fileStr) && fileNameStr != null && !"".equals(fileNameStr)) {
            String[] fileNameList = fileNameStr.split(",");
            String[] fileList = fileStr.split(",");
            for (int i = 0; i < fileList.length; i++) {
                FileView fileView = new FileView();
                fileView.setRelativePath(fileList[i]);
                fileView.setFileName(fileNameList[i]);
                fileView.setAbsolutePath(prePath + fileList[i]);
                fileViews.add(fileView);
            }
        }
        return fileViews;
    }

    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }


    /**
     * 解析地址
     *
     * @param address
     * @return
     */
    public static Map<String, String> addressResolutionOne(String address) {
        /*
         * java.util.regex是一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包。它包括两个类：Pattern和Matcher Pattern
         *    一个Pattern是一个正则表达式经编译后的表现模式。 Matcher
         *    一个Matcher对象是一个状态机器，它依据Pattern对象做为匹配模式对字符串展开匹配检查。
         *    首先一个Pattern实例订制了一个所用语法与PERL的类似的正则表达式经编译后的模式，然后一个Matcher实例在这个给定的Pattern实例的模式控制下进行字符串的匹配工作。
         */

        String provinceRegex = "(?<province>[^省]+自治区|.*?省|.*?行政区)";
        String cityRegex = "(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)";
        String countyRegex = "(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
//        String regex="((?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
//        Matcher m= Pattern.compile(regex).matcher(address);

        String province = null, city = null, county = null, town = null, village = null;
        List<Map<String, String>> table = new ArrayList<Map<String, String>>();
//        Map<String, String> row = null;

        Matcher m = Pattern.compile(provinceRegex).matcher(address);
        Map<String, String> row = new LinkedHashMap<String, String>();
        while (m.find()) {
            province = m.group("province");
            row.put("province", province == null ? "" : province.trim());
        }

        m = Pattern.compile(cityRegex).matcher(address);
        while (m.find()) {
            city = m.group("city");
            row.put("city", city == null ? "" : city.trim());
        }
        m = Pattern.compile(countyRegex).matcher(address);
        while (m.find()) {
            county = m.group("county");
            row.put("county", county == null ? "" : county.trim());
            town = m.group("town");
            row.put("town", town == null ? "" : town.trim());
            village = m.group("village");
            row.put("village", village == null ? "" : village.trim());
        }


//        while(m.find()){
//            row=new LinkedHashMap<String,String>();
//            province=m.group("province");
//            row.put("province", province==null?"":province.trim());
//            city=m.group("city");
//            row.put("city", city==null?"":city.trim());
//            county=m.group("county");
//            row.put("county", county==null?"":county.trim());
//            town=m.group("town");
//            row.put("town", town==null?"":town.trim());
//            village=m.group("village");
//            row.put("village", village==null?"":village.trim());
//            table.add(row);
//        }
        return row;
    }

    /**
     * 解析地址
     * @param address
     * @return
     */
    public static List<Map<String,String>> addressResolutionTwo(String address) {
        /*
         * java.util.regex是一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包。它包括两个类：Pattern和Matcher Pattern
         *    一个Pattern是一个正则表达式经编译后的表现模式。 Matcher
         *    一个Matcher对象是一个状态机器，它依据Pattern对象做为匹配模式对字符串展开匹配检查。
         *    首先一个Pattern实例订制了一个所用语法与PERL的类似的正则表达式经编译后的模式，然后一个Matcher实例在这个给定的Pattern实例的模式控制下进行字符串的匹配工作。
         */
        String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m = Pattern.compile(regex).matcher(address);
        String province = null, city = null, county = null, town = null, village = null;
        List<Map<String, String>> table = new ArrayList<Map<String, String>>();
        Map<String, String> row = null;
        while (m.find()) {
            row = new LinkedHashMap<String, String>();
            province = m.group("province");
            row.put("province", province == null ? "" : province.trim());
            city = m.group("city");
            row.put("city", city == null ? "" : city.trim());
            county = m.group("county");
            row.put("county", county == null ? "" : county.trim());
            town = m.group("town");
            row.put("town", town == null ? "" : town.trim());
            village = m.group("village");
            row.put("village", village == null ? "" : village.trim());
            table.add(row);
        }
        return table;
    }


        /**
         * 解析地址
         *
         * @param address
         * @return
         */
    public static List<Map<String, String>> addressResolutionThree(String address) {
//        String regex = "((?<province>[^省]+省|.+自治区)|上海|北京|天津|重庆)|(?<city>[^市]+市|.+自治州)|(?<county>[^县]+县|.+区|.+镇|.+局)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m = Pattern.compile(regex).matcher(address);
        String province = "", city = "", county = "", town = "", village = "";
        List<Map<String, String>> table = new ArrayList<Map<String, String>>();
        Map<String, String> row = new LinkedHashMap<String, String>();
        while (m.find()) {
//            row=new LinkedHashMap<String,String>();
            String provinceGroup = m.group("province")==null?"":m.group("province");
            province = province.length() > 0 ? province : provinceGroup.trim();

            String cityGroup = m.group("city")==null?"":m.group("city");
            city = city.length() > 0 ? city : cityGroup.trim();

            String countyGroup = m.group("county")==null?"":m.group("county");
            county = county.length() > 0 ? county : countyGroup.trim();

            String townGroup = m.group("town")==null?"":m.group("town");
            town = town.length()>0 ? town : townGroup.trim();

            String villageGroup = m.group("village")==null?"":m.group("village");
            village = village.length()>0 ? village : villageGroup.trim();
//            table.add(row);
        }
        row.put("province", province);
        row.put("city", city);
        row.put("county", county);
        row.put("town", town);
        row.put("village", village);
        table.add(row);
        return table;
    }


    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    public static void main(String[] args) {
        String str = "湖北省恩施土家族苗族自治州恩施市";
//        Map<String, String> table = addressResolution(str);
        List<Map<String, String>> table = addressResolutionThree(str);
        System.out.println(table);
        System.out.println(table.get(0).get("province"));
        System.out.println(table.get(0).get("city"));
        System.out.println(table.get(0).get("county"));
        System.out.println(table.get(0).get("town"));
        System.out.println(table.get(0).get("village"));

    }
}
