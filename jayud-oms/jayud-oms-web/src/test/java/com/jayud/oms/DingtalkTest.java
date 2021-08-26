package com.jayud.oms;

import cn.hutool.json.JSONObject;
import com.jayud.oms.service.DingtalkMsgService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DingtalkTest {

    @Autowired
    private DingtalkMsgService dingtalkMsgService;//钉钉消息服务


    /**
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

}
