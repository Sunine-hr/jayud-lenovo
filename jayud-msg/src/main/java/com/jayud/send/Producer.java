package com.jayud.send;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 将数据丢给kafka
 * @author william
 * @description
 * @Date: 2020-09-17 15:06
 */
public class Producer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${kafka.consumer.topic:}")
    String topic;

    /**
     * 提供发送接口
     * @param value
     * @return
     */
    public ListenableFuture send(String key, String value) {
        return  kafkaTemplate.send(this.topic,key,value);
    }

    public ListenableFuture send(String topic, String key, String value) {
        return kafkaTemplate.send(topic, key, value);
    }
}
