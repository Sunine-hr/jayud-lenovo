package com.jayud.send;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送消息
 *
 * @author larry
 * 2019年6月12日14:23:20
 */

@RequestMapping(value = "/kafka")
@RestController
@SuppressWarnings("unchecked")
public class ProducerController {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 提供发送接口
     *
     * @param topic
     * @param value
     */
    @RequestMapping(value = "/producer", method = RequestMethod.POST)
    public void consume(String topic, String key, String value) {
        kafkaTemplate.send(topic, key, value);
    }
}
