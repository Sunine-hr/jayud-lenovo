package com.jayud.oms;

import cn.hutool.json.JSONObject;
import com.jayud.oms.feign.MsgClient;
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
    private MsgClient msgClient;//消息服务调用


    /**
     * 自定义钉钉应用的id和秘钥，并使用msg服务调用   测试成功！推荐！
     *
     * 通过手机号给钉钉用户发送消息 (自定义的业务接口)
     * 自定义钉钉应用的id和秘钥
     * 通过服务调用发送消息
     */
    @Test
    public void sendMessageByMobileTest(){
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

    /**
     * 通过openid，给微信小程序用户发送订阅消息
     */
    @Test
    public void sendMessageByOpenidTest(){
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("touser", "oIjjU5DQ8BGQfWKLjn43xtAJUtGc");
        msgMap.put("template_id", "XPh1K5M1tWTuDyG2g26zgurUIHIEw5dDX50DxDJCOAs");
        msgMap.put("page", "index");
        msgMap.put("lang", "zh_CN");

        Map<String, Object> data = new HashMap<>();
        data.put("thing2", new JSONObject().set("value", "集司码头-new-5"));
        data.put("thing3", new JSONObject().set("value", "请前往查看接单详情，沟通操作细节！"));
        data.put("thing4", new JSONObject().set("value", "某某项目v.2021"));
        data.put("thing11", new JSONObject().set("value", "1板+10件+11箱"));
        data.put("thing1", new JSONObject().set("value", "2021年1月14日上午8：00"));

        msgMap.put("data", data);
        log.debug(String.format("开始发送订阅消息给微信小程序..."));
        JSONObject jsonObject = msgClient.sendMessageByOpenid(msgMap);
        System.out.println(jsonObject);
    }


}
