package com.jayud.common;

import com.jayud.common.enums.ExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.httpclient.HttpStatus;

import java.io.Serializable;

@ApiModel("swagger返回的通用接口数据")
public class ApiResult<T> implements Serializable {
    @ApiModelProperty(value = "返回码", dataType = "int")
    private int code;
    @ApiModelProperty(value = "错误信息", dataType = "String")
    private String msg = "success";
    @ApiModelProperty(value = "返回數據")
    private T data;

    public ApiResult() {
    }

    public ApiResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static ApiResult ok() {
        return new ApiResult(HttpStatus.SC_OK, "success");
    }

    public static ApiResult error() {
        return new ApiResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, "fail");
    }

    public static ApiResult error(int code, String msg) {
        return new ApiResult(code, msg);
    }

    public static ApiResult error(String msg) {
        return new ApiResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static ApiResult error(Exception e) {
        return new ApiResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }

    public static ApiResult ok(Object data) {
        return new ApiResult<>(HttpStatus.SC_OK, "success", data);
    }

    public ApiResult(int code) {
        this.code = code;
    }

    public ApiResult(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.msg = exceptionEnum.getMsg();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;

    }
    public boolean isOk() {
        boolean flag = false;
        if (HttpStatus.SC_OK == this.getCode()) {
            flag = true;
        }
        return flag;

    }

}
