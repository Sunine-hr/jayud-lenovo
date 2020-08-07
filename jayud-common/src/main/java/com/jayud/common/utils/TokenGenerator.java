package com.jayud.common.utils;

import com.jayud.common.exception.WebBasException;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * @anthor Satellite
 * TokenGenerator
 * token 生成类
 * http://www.javalow.com
 * @date 2018-11-19-22:57
 **/
public class TokenGenerator {

    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    public static String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    public static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            throw new WebBasException("生成Token失败", e);
        }
    }
}
