package com.jayud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;
/**
 * @author william
 * @description
 * @Date: 2020-09-11 14:26
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableKafka
public class JayudMsgApplication {
}
