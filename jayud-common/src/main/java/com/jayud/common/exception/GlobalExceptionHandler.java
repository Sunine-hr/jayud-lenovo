package com.jayud.common.exception;

import com.jayud.common.BaseApiResult;
import com.jayud.common.VivoApiResult;
import com.jayud.common.ApiResult;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数验证统一异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String vivoBasePackage = "com.jayud.airfreight.controller.ReceiveVivoController";
    private final String vivoBaseRequestmap = "/airfreight/fromVivo";


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseApiResult validationErrorHandler(ConstraintViolationException ex) {
        //如果没有，则可能又多个
        List<String> errorInformation = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return new ApiResult<>(400, errorInformation.toString());
    }

    // 验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseApiResult validException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        String defaultMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        if (ex.getParameter().getContainingClass().getName().contains(vivoBasePackage)) {
            return new VivoApiResult(defaultMessage);
        }
        return new ApiResult(getStatus(request).value(), defaultMessage);
    }

    //处理vivo手动抛出的标准异常
    @ExceptionHandler(VivoApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseApiResult handleVivoException(VivoApiException ex) {
        return new VivoApiResult(ex.getMessage());
    }

    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public BaseApiResult handle401(ShiroException e) {
        return new ApiResult(401, e.getMessage());
    }

    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public BaseApiResult handle401() {
        return new ApiResult(401, "没有权限");
    }

//    @ExceptionHandler(CommonException.class)
//    public ApiResult handlerException(CommonException e) {
//        ExceptionEnum exceptionEnum = e.getExceptionEnum();
//        return new ApiResult(exceptionEnum.getCode(), exceptionEnum.getMsg());
//    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseApiResult globalException(HttpServletRequest request, Throwable ex) {
        if (request.getRequestURI().contains(vivoBaseRequestmap)) {
            return new VivoApiResult(ex.getMessage());
        }
        return new ApiResult(getStatus(request).value(), ex.getMessage());
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
