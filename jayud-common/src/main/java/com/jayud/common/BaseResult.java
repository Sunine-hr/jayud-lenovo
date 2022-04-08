package com.jayud.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.constant.SysTips;
import com.jayud.common.enums.ResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.http.HttpStatus;

import java.io.Serializable;

/**
 * @author ciro
 * @date 2022/2/16 17:20
 * @description: 返回对象
 */
@Data
@ApiModel(value = "接口返回对象", description = "接口返回对象")
public class BaseResult<T> implements Serializable {
    private static final long serialVersionUID = 9221717396155643545L;
    /**
     * 成功标志
     */
    @ApiModelProperty(value = "成功标志")
    private boolean success = true;
    /**
     * 返回处理消息
     */
    @ApiModelProperty(value = "返回处理消息")
    private String msg = "操作成功！";
    /**
     * 返回代码
     */
    @ApiModelProperty(value = "返回代码")
    private Integer code = 200;
    /**
     * 返回数据对象 data
     */
    @ApiModelProperty(value = "返回数据对象")
    private T result;
    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();

    @JsonIgnore
    private String onlTable;

    public BaseResult() {
    }

    /**
     * 成功提示
     *
     * @return 返回对象
     */
    public static <T> BaseResult<T> ok() {
        BaseResult<T> r = new BaseResult<T>();
        r.setSuccess(true);
        r.setMsg(SysTips.SUCCESS_MSG);
        r.setCode(HttpStatus.SC_OK);
        return r;
    }

    /**
     * 成功提示返回数据
     *
     * @param data 数据
     * @return 返回对象
     */
    public static <T> BaseResult<T> ok(T data) {
        BaseResult<T> r = new BaseResult<T>();
        r.setSuccess(true);
        r.setCode(HttpStatus.SC_OK);
        r.setResult(data);
        return r;
    }

    /**
     * 成功提示返回信息
     *
     * @param msg 提示信息
     * @return 返回对象
     */
    public static <T> BaseResult<T> ok(String msg) {
        BaseResult<T> r = new BaseResult<T>();
        r.setSuccess(true);
        r.setCode(HttpStatus.SC_OK);
        r.setMsg(msg);
        return r;
    }

    /**
     * 成功提示既返回信息又返回数据
     *
     * @param msg  提示信息
     * @param data 数据
     * @return 返回对象
     */
    public static <T> BaseResult<T> ok(String msg, T data) {
        BaseResult<T> r = new BaseResult<T>();
        r.setSuccess(true);
        r.setCode(HttpStatus.SC_OK);
        r.setMsg(msg);
        r.setResult(data);
        return r;
    }

    /**
     * 错误提示返回默认信息
     *
     * @return 返回对象
     */
    public static <T> BaseResult<T> error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, SysTips.ERROR_MSG);
    }

    /**
     * 错误提示返回信息
     *
     * @param msg 提示信息
     * @return 返回对象
     */
    public static <T> BaseResult<T> error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    /**
     * 错误提示既返回代码又返回信息
     *
     * @param code 状态码
     * @param msg  提示信息
     * @return 返回对象
     */
    public static <T> BaseResult<T> error(int code, String msg) {
        BaseResult<T> r = new BaseResult<T>();
        r.setCode(code);
        r.setMsg(msg);
        r.setSuccess(false);
        return r;
    }

    /**
     * 错误提示返回信息
     *
     * @param msg 提示信息
     * @return
     */
    public <T> BaseResult<T> error500(String msg) {
        BaseResult<T> r = new BaseResult<T>();
        r.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        r.setMsg(msg);
        r.setSuccess(false);
        return r;
    }

    /**
     * @description token过期
     * @author  ciro
     * @date   2022/2/26 13:42
     * @param: msg
     * @return: com.jayud.common.BaseResult<T>
     **/
    public static <T> BaseResult<T> unAuthorized(String msg) {
        BaseResult<T> r = new BaseResult<T>();
        r.setSuccess(false);
        r.setCode(ResultEnum.UNAUTHORIZED.getCode());
        r.setMsg(ResultEnum.UNAUTHORIZED.getMessage());
        return r;
    }

    public static <T> BaseResult<T> error(String msg, T data) {
        BaseResult<T> r = new BaseResult<T>();
        r.setSuccess(true);
        r.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        r.setMsg(msg);
        r.setResult(data);
        return r;
    }
}
