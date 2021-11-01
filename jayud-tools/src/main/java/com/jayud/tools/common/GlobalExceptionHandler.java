package com.jayud.tools.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 * @author ZJ
 *
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ResponseBody
	@ExceptionHandler(RuntimeException.class)
	public AjaxResult globalException(HttpServletResponse response, RuntimeException ex) {
		log.info("请求错误：" + ex.getMessage());
		return AjaxResult.error(ex.getMessage());
	}

}