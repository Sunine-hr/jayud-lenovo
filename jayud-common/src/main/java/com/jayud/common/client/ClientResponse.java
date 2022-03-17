package com.jayud.common.client;

import java.io.Serializable;

/**
 * 处理微服务之间的调用返回结果
 */
public class ClientResponse implements Serializable {

	private static final long serialVersionUID = 2271573785794685249L;
	// 调用返回默认成功
	private ResultCode code = ResultCode.SUCCESS;
	// 调用返回信息
	private String message;

	public ClientResponse(ResultCode code) {
		this.code = code;
	}

	public ClientResponse(ResultCode code, String message) {
		this.code = code;
		this.message = message;
	}

	public boolean isSuccess() {
		return code == ResultCode.SUCCESS;
	}

	public ResultCode getCode() {
		return code;
	}

	public void setCode(ResultCode code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
