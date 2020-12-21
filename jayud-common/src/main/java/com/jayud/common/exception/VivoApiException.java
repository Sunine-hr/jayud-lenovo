package com.jayud.common.exception;

import lombok.Getter;

/**
 * vivo专门使用的断言类抛出的异常，交给全局异常处理后，抛出vivo的异常格式
 *
 * @author william
 * @description
 * @Date: 2020-09-16 14:39
 */
@Getter
public class VivoApiException extends RuntimeException {
    private String message;
    private Integer status;

    public VivoApiException(String message) {
        this.status = 0;
        this.message = message;
    }
    VivoApiException(){
        this.status=0;
        this.message= super.getMessage();
    }
    public VivoApiException(String msg, Throwable e) {
        super(msg, e);
        this.message = msg;
    }
}
