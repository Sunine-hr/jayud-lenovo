package com.jayud.api;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.jayud.service.WeixinMiniProgramMsgServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 微信小程序消息api 调用
 */
@RequestMapping(value = "/api/msg/weixinminiprogram")
@RestController
@Slf4j
public class WeixinMiniProgramMsgController {


    @Autowired
    private WeixinMiniProgramMsgServer weixinMiniProgramMsgServer;

    /**
     * 通过openid，给微信小程序用户发送订阅消息
     * 注意：
     *      小程序页面，调起授权，出现一次，授权一次，发送一次订阅消息（授权一次，多发送无效；选择了“总是保持以上选择，不再询问”，也仅仅是授权了一次，第二次发送则用户拒绝消息）
     *      需要引导用户授权
     * 示例：
     {
     "touser": "oIjjU5DQ8BGQfWKLjn43xtAJUtGc",
     "template_id": "XPh1K5M1tWTuDyG2g26zgurUIHIEw5dDX50DxDJCOAs",
     "page": "index",
     "lang": "zh_CN",
     "data": {
         "thing2": {
         "value": "集司码头-new-5"
         },
         "thing3": {
         "value": "请前往查看接单详情，沟通操作细节！"
         },
         "thing4": {
         "value": "某某项目v.2021"
         },
         "thing11": {
         "value": "1板+10件+11箱"
         },
         "thing1": {
         "value": "2021年1月14日上午8：00"
         }
        }
     }
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sendMessageByOpenid")
    public JSONObject sendMessageByOpenid(@RequestBody Map<String, Object> param){
        String touser = MapUtil.getStr(param, "touser");//接收者（用户）的 openid
        Map<String, Object> body = param;
        return weixinMiniProgramMsgServer.sendMessageByOpenid(touser, body);
    }

}
