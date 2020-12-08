package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * kafka topic-key对应关系枚举
 *
 * @author william
 * @description
 * @Date: 2020-09-17 16:53
 */
@Getter
@AllArgsConstructor
public enum KafkaMsgEnums {
    //财务-应收应付异步处理
    FINANCE_CUSTOMS_RECEIVABLE("financeTest", "customs-receivable"),
    FINANCE_CUSTOMS_PAYABLE("financeTest", "customs-payable"),
    //空运订单异步处理
    VIVO_FREIGHT_AIR_MESSAGE_ONE("vivoAir", "Confirm-booking-info");
    private String topic;
    private String key;
}
