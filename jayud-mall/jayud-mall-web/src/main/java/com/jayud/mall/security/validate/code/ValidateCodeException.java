package com.jayud.mall.security.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * <p>定义一个验证码类型的异常类</p>
 * <p>继承的是`AuthenticationException`</p>
 */
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = 5022575393500654458L;

    ValidateCodeException(String message) {
        super(message);
    }
}
