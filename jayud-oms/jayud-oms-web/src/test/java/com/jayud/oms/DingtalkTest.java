package com.jayud.oms;

import cn.hutool.json.JSONObject;
import com.jayud.oms.feign.MsgClient;
import com.jayud.oms.service.DingtalkMsgService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DingtalkTest {

    @Autowired
    private DingtalkMsgService dingtalkMsgService;//钉钉消息服务
    @Autowired
    private MsgClient msgClient;//消息服务调用


    /**
     * 1.使用默认 钉钉应用id及其秘钥 （默认jayud应用）    测试成功！不推荐这种方式！
     *
     * 通过手机号给钉钉用户发送消息 (自定义的业务接口)
     */
    @Test
    public void sendMessageByMobileTest1(){
        String mobile = "18186330408";
        String message = "大家好，这是工作通知测试，只针对技术部，收到请忽略。";
        JSONObject jsonObject = dingtalkMsgService.sendMessageByMobile(mobile, message);
        System.out.println(jsonObject);
    }


    /**
     * 2.自定义钉钉应用的id和秘钥      测试成功！不推荐这种方式！
     *
     * 通过手机号给钉钉用户发送消息 (自定义的业务接口)
     * 自定义钉钉应用的id和秘钥
     */
    @Test
    public void sendMessageByMobileTest2(){
        String agentid = "1279251144";
        String appkey = "ding07nvmiueit3awmod";
        String appsecret = "zj4JdyA8OcvYuBf7cLTaH87S91vuMtI2PKlAtI_Ze0Q7haQ3ux6ysryhT6P-JGM4";

        String mobile = "18186330408";
        String message = "大家好，这是工作通知测试，只针对技术部，收到请忽略。";
        JSONObject jsonObject = dingtalkMsgService.sendMessageByMobile(mobile, message, agentid, appkey, appsecret);
        System.out.println(jsonObject);
    }

    /**
     * 3.自定义钉钉应用的id和秘钥，并使用msg服务调用   测试成功！推荐！
     *
     * 通过手机号给钉钉用户发送消息 (自定义的业务接口)
     * 自定义钉钉应用的id和秘钥
     * 通过服务调用发送消息
     */
    @Test
    public void sendMessageByMobileTest3(){
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("mobile", "18186330408");
        msgMap.put("message", "大家好，这是工作通知测试，只针对技术部，收到请忽略。");
        msgMap.put("agentid", "1279251144");
        msgMap.put("appkey", "ding07nvmiueit3awmod");
        msgMap.put("appsecret", "zj4JdyA8OcvYuBf7cLTaH87S91vuMtI2PKlAtI_Ze0Q7haQ3ux6ysryhT6P-JGM4");
        log.debug(String.format("开始发送消息给钉钉..."));
        JSONObject jsonObject = msgClient.sendMessageByMobile(msgMap);
        System.out.println(jsonObject);

    }


}
