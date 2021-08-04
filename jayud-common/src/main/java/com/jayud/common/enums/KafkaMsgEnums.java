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
    VIVO_FREIGHT_AIR_MESSAGE_ONE("vivoAir", "Confirm-booking-info"),//确认订舱信息
    VIVO_FREIGHT_AIR_MESSAGE_TWO("vivoAir", "tracking-push"),//跟踪信息
    VIVO_FREIGHT_AIR_MESSAGE_THREE("vivoAir", "bill-lading-info-push"),//跟踪信息
    VIVO_FREIGHT_AIR_MESSAGE_FOUR("vivoAir", "receivable-costs-push"),//应收费用推送

    VIVO_FREIGHT_TMS_MESSAGE_ONE("vivoTms", "push-vehicle-info"),//推送派车消息
    VIVO_FREIGHT_TMS_MESSAGE_TWO("vivoTms", "receivable-costs-push"),//应收费用推送
    // 报关发送邮件异步处理
    CUSTOM_SEND_EMAIL("sendEmail","customs-send-email"),

    // 消息推送任务
    MESSAGE_PUSH_TASK("msgPushTask","msg-push-task")
    ;

    private String topic;
    private String key;
}
