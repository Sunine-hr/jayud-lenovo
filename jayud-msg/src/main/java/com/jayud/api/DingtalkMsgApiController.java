package com.jayud.api;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.jayud.service.DingtalkMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 钉钉消息api 调用
 */
@RequestMapping(value = "/api/msg/dingtalk")
@RestController
@Slf4j
public class DingtalkMsgApiController {

    @Autowired
    private DingtalkMsgService dingtalkMsgService;

    /**
     * 通过手机号给钉钉用户发送消息 (自定义的业务接口)-自定义钉钉应用的id和秘钥
     * {
     *  mobile 接收消息用户的手机号
     *  message 发送的消息(文本消息，content内容，建议500字符以内。)
     *  agentid 发送消息时使用的微应用的AgentID
     *  appkey 应用的唯一标识key
     *  appsecret 应用的密钥
     * }
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sendMessageByMobile")
    public JSONObject sendMessageByMobile(@RequestBody Map<String, String> param){
        String mobile = MapUtil.getStr(param, "mobile");
        String message = MapUtil.getStr(param, "message");
        String agentid = MapUtil.getStr(param, "agentid");
        String appkey = MapUtil.getStr(param, "appkey");
        String appsecret = MapUtil.getStr(param, "appsecret");
        return dingtalkMsgService.sendMessageByMobile(mobile, message, agentid, appkey, appsecret);
    }


}
