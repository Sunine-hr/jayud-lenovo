package com.jayud.common.exception;

import com.jayud.common.client.ResultCode;

/**
 * 业务服务的统一异常处理
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 2359767895161832954L;
	private final ResultCode resultCode; // 微服务之间的调用返回结果

	public ResultCode getResultCode() {
		return resultCode;
	}

	public ServiceException(String message) {
		super(message);
		this.resultCode = ResultCode.FAILURE;
	}

	public ServiceException(ResultCode resultCode) {
		super(resultCode.getMsg());
		this.resultCode = resultCode;
	}

	public ServiceException(ResultCode resultCode, String msg) {
		super(msg);
		this.resultCode = resultCode;
	}

	public ServiceException(ResultCode resultCode, Throwable cause) {
		super(cause);
		this.resultCode = resultCode;
	}

	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
		this.resultCode = ResultCode.FAILURE;
	}

}
