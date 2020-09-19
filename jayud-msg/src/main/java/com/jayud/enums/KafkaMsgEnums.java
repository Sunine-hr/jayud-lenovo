package com.jayud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Objects;

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
    FINANCE_CUSTOMS_RECEIVABLE("financeTest", "customs-receivable"),
    FINANCE_CUSTOMS_PAYABLE("financeTest", "customs-payable"),
    ;
    private String topic;
    private String key;
}
