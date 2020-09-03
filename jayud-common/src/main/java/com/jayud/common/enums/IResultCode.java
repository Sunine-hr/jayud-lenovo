package com.jayud.common.enums;

/**
 * 公共结果代码接口
 * @author bocong.zheng
 */
public interface IResultCode {

    /**
     * 获取代码
     * @return
     */
    Integer getCode();

    /**
     * 获取信息
     * @return
     */
    String getMessage();
}
