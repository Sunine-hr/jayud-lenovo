package com.jayud.common.exception;

/**
 * @anthor Satellite
 * WebBasException
 * 自定义异常
 * http://www.javalow.com
 * @date 2018-11-18-15:33
 **/
public class WebBasException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public WebBasException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public WebBasException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public WebBasException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public WebBasException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
