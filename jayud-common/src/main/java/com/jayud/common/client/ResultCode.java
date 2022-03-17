package com.jayud.common.client;

import javax.servlet.http.HttpServletResponse;

/**
 * 微服务之间的调用返回结果的编码信息
 */
public enum ResultCode {

	SUCCESS(HttpServletResponse.SC_OK, "Operation is Successful"), FAILURE(HttpServletResponse.SC_BAD_REQUEST, "Biz Exception"),
	UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "Request Unauthorized"), NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "404 Not Found"),
	MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "Message Can't be Read"),
	METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Supported"),
	MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Media Type Not Supported"),
	REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "Request Rejected"),
	INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),
	PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "Missing Required Parameter"),
	PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Type Mismatch"),
	PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Binding Error"),
	PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Validation Error");

	final int code;// 编码
	final String msg;// 编码对应的信息

	private ResultCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
