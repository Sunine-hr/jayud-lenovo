package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum implements IResultCode {

    UNKNOWN_ERROR(-1, "未知错误"),

    SUCCESS(0, "成功"),

    PARAM_ERROR(1,"参数不正确"),

    UPDATE_ERROR(2,"更新数据失败"),

    SAVE_ERROR(3,"更新数据失败"),

    LOGIN_FAIL(25, "登录失败, 登录信息不正确"),

    VALIDATE_FAILED(404, "参数检验失败"),

    UNAUTHORIZED(401, "暂未登录或token已经过期"),

    FORBIDDEN(403, "没有相关权限"),

    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),

    UNSUPPORTED_MEDIA_TYPE(415, "不支持当前媒体类型"),

    UNPROCESSABLE_ENTITY(422, "所上传文件大小超过最大限制，上传失败"),

    INTERNAL_SERVER_ERROR(500, "服务内部异常"),


    OPR_FAIL(10001, "操作失败"),
    ;
    private Integer code;
    private String message;

}
