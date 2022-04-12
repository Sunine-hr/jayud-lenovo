package com.jayud.wms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author ciro
 * @date 2022/4/12 10:47
 * @description: 出库通知单状态
 */
@Getter
@AllArgsConstructor
public enum OutboundNoticeOrdertSatus {

    CREATE(1,"创建"),
    ASSIGNED(2,"已出库"),
    CHANGE(3,"转出库");


    /**
     * 类型
     */
    private final Integer type;

    /**
     * 信息
     */
    private final String msg;

    public static String getMsg(Integer code) {
        for (OutboundNoticeOrdertSatus value : values()) {
            if (Objects.equals(code, value.getMsg())) {
                return value.getMsg();
            }
        }
        return "";
    }


}
