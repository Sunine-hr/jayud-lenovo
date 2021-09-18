package com.jayud.tools.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信校验工具
 */
public class WeixinCheckUtil {

    public static final String TOKEN ="jayud";//这里随便填入32位以内的数字英文，也就是token

    /**
     * 验证签名
     * @param signature  验证签名的数据
     * @param timestamp  参数
     * @param nonce  参数
     * @return  boolean
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {

        //TOKEN
        //与token 比较
        String[] arr = new String[] { TOKEN, timestamp, nonce };

        // 将token、timestamp、nonce三个参数进行字典排序
        Arrays.sort(arr);

        StringBuilder content = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }

        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");

            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        content = null;
        // 将sha1加密后的字符串可与signature对比
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }


    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return  String
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }




    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return  String
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

}
