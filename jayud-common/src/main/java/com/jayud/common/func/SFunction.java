package com.jayud.common.func;

import java.io.Serializable;

/**
 * lambda函数
 */
@FunctionalInterface
public interface SFunction<T> extends Serializable {
    Object get(T source);
}
