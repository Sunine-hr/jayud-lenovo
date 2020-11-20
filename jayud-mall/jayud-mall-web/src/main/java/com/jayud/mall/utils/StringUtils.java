package com.jayud.mall.utils;

/**
 * String类型，转换为 utf-8
 * <p>下载或者导出excel，设置中文名称有用到</p>
 * <p>url编码解码</p>
 */
public class StringUtils {

    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).
                            toUpperCase());
                }
            }
        }
        return sb.toString();
    }
}
