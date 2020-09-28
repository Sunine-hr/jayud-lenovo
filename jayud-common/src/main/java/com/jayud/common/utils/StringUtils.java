package com.jayud.common.utils;

import io.netty.util.internal.StringUtil;
import org.apache.http.util.TextUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        companyName = companyName.replaceAll("（", StringUtil.EMPTY_STRING).replaceAll("）",StringUtil.EMPTY_STRING);
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
     * @param randomNum 随机数长度
     * @return
     */
    public static String loadNum(String head,int randomNum){
        int roandm = 0;
        char [] str = {'0','1','2','3','4','5','6','7','8','9'};
        StringBuffer stringBuffer = new StringBuffer("");
        Random r = new Random();
        for(int count = 0;count < randomNum;count++){
            //生成10以内的随机整数
            roandm = Math.abs(r.nextInt(10));
            if(roandm>=0 || roandm<str.length){
                stringBuffer.append(""+str[roandm]);
            }
        }
        return head+""+stringBuffer.toString();
    }

    public static String generate(){
        char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'P','Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z','0','1','2','3','4','5','6','7','8','9'};
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
     * @param fileViewList
     * @return
     */
    public static String getFileStr(List<FileView> fileViewList){
        String fileStr = "";
        if(fileViewList == null || fileViewList.size() == 0){
            return fileStr;
        }
        StringBuilder sb = new StringBuilder();
        for (FileView fileView : fileViewList) {
            sb.append(fileView.getRelativePath()).append(",");
        }
        if(!"".equals(String.valueOf(sb))) {
            fileStr = sb.substring(0, sb.length() - 1);
        }
        return fileStr;
    }

    /**
     * 把字符串解析成文件数组
     * @param fileStr
     * @return
     */
    public static List<FileView> getFileViews(String fileStr,String prePath){
        List<FileView> fileViews = new ArrayList<>();
        if(fileStr != null && !"".equals(fileStr)){
            String[] fileList = fileStr.split(",");
            for(String str : fileList){
                int index = str.lastIndexOf("/");
                FileView fileView = new FileView();
                fileView.setRelativePath(str);
                fileView.setFileName(str.substring(index + 1, str.length()));
                fileView.setAbsolutePath(prePath + str);
                fileViews.add(fileView);
            }
        }
        return fileViews;
    }

    public static void main(String[] args) {
    }
}
