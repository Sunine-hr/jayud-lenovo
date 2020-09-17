package com.jayud.common.exception;


import com.jayud.common.enums.IResultCode;

/**
 * 断言处理类，用于抛出各种API异常
 * Created by macro on 2020/2/27.
 */
public class Asserts {

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IResultCode errorCode) {
        throw new ApiException(errorCode);
    }

    public static void fail(IResultCode errorCode, String message) {
        throw new ApiException(errorCode.getCode(),message);
    }

    public static void fail(int code,String message) {
        throw new ApiException(code,message);
    }

    public static void vivoFail(String message) {
        throw new VivoApiException(message);
    }
}
