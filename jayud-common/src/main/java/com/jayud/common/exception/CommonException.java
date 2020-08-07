package com.jayud.common.exception;

import com.jayud.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hanbin
 * @version 1.0.0
 * @ClassName CommonException.java
 * @Description TODO
 * @createTime 2019年11月21日 13:47:00
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonException extends RuntimeException {
    /**
     * 建议使用code来返回信息
     */
    private ExceptionEnum exceptionEnum;

}
