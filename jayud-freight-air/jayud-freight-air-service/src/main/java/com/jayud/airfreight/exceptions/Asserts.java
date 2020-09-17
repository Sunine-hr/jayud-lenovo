package com.jayud.airfreight.exceptions;


import com.jayud.common.enums.IResultCode;
import com.jayud.common.exception.ApiException;

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

}
