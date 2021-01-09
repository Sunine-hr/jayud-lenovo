package com.jayud.common.exception;

import com.jayud.common.enums.ResultEnum;

/**
 * @anthor Satellite
 * WebBasException
 * 业务异常
 * http://www.javalow.com
 * @date 2018-11-18-15:33
 **/
public class JayudBizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String msg;
    private Integer code = 500;


    public JayudBizException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
    }

    public JayudBizException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public JayudBizException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public JayudBizException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public JayudBizException(String msg, Integer code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public JayudBizException(String msg, int code, Throwable e) {
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
