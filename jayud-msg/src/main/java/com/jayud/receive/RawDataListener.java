package com.jayud.receive;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * kafka监听
 *
 * @author larry
 * 2019-6-12 14:23:41
 */
@Component
@RefreshScope
public class RawDataListener {
    @Autowired
    private CreditMsgClient creditMsgClient;
    @Autowired
    private PlatformMsgClient platformMsgClient;
    @Value("${send.topic.creditAdminKey:}")
    private String creditAdminKey;
    @Value("${send.topic.platformKey:}")
    private String platformKey;
    /**
     * 实时获取kafka数据(生产一条，监听生产topic自动消费一条)
     *
     * @param record
     * @throws IOException
     */
    @KafkaListener(topics = {"${kafka.consumer.topic:}"})
    public void listen(ConsumerRecord<?, ?> record) throws IOException {
        String value = (String) record.value();
        String key = (String) record.key();
        if (key.equals(creditAdminKey)) {
            creditMsgClient.customer(value);
        }
        if(key.equals(platformKey)){
            Map map =new HashMap(16);
            map.put("value",value);
            platformMsgClient.pushMsg(map);
        }
    }
}