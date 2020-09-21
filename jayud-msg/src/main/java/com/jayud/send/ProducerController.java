package com.jayud.send;

import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
     */
    @RequestMapping(value = "/producer", method = RequestMethod.POST)
    public CommonResult consume(@RequestBody Map<String, String> param) {
        String topic = MapUtil.getStr(param, "topic");
        String key = MapUtil.getStr(param, "key");
        String value = MapUtil.getStr(param, "msg");
//        return CommonResult.success();
        try {
            kafkaTemplate.send(topic, key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(ResultEnum.INTERNAL_SERVER_ERROR, "发送失败");
        }
        return CommonResult.success();
    }
}
