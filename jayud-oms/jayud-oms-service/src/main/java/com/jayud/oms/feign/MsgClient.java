package com.jayud.oms.feign;

import cn.hutool.json.JSONObject;
import com.jayud.common.config.FeignRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author william
 * @description
 * @Date: 2020-09-19 10:32
 */
@FeignClient(value = "jayud-msg-config", configuration = FeignRequestInterceptor.class)
public interface MsgClient {

    @RequestMapping(value = "/kafka/producer", method = RequestMethod.POST)
    Map<String, String> consume(@RequestBody Map<String, String> param);

    /**
     * 根据手机号，发送钉钉应用消息
     */
    @RequestMapping(value = "/api/msg/dingtalk/sendMessageByMobile", method = RequestMethod.POST)
    JSONObject sendMessageByMobile(@RequestBody Map<String, String> param);

}
