package com.jayud.common.utils;

import java.text.MessageFormat;

/**
 * @author larry
 * @date 2019/10/16
 */
public class MessageUtils {

    /**
     * 格式化消息
     *
     * @param module
     * @param array
     * @return
     */
    public static String getMessage(String module, Object[] array) {
        MessageFormat messageFormat = new MessageFormat(module);
        return messageFormat.format(array);
    }
}