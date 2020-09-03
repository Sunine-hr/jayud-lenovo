package com.jayud.common.exception;


import com.jayud.common.enums.IResultCode;
import lombok.Getter;

/**
 * 自定义API异常
 * Created by macro on 2020/2/27.
 */
@Getter
public class ApiException extends RuntimeException {

    private Integer code;

    public ApiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(IResultCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ApiException(String message) {
        super(message);
    }


}
