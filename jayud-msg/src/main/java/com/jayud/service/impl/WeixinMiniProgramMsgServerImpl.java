package com.jayud.service.impl;

import cn.hutool.json.JSONObject;
import com.jayud.service.WeixinMiniProgramMsgServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class WeixinMiniProgramMsgServerImpl implements WeixinMiniProgramMsgServer {



    /**
     * 1.接口调用凭证 /getAccessToken /auth.getAccessToken
     * @param grant_type 填写 client_credential
     * @param appid 小程序唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且帐号没有异常状态）
     * @param secret 小程序唯一凭证密钥，即 AppSecret，获取方式同 appid
     * @return
     */
    @Override
    public JSONObject getAccessToken(String grant_type, String appid, String secret) {
        return null;
    }

    /**
     * 2.登录 /code2Session /auth.code2Session
     * @param appid 小程序 appId
     * @param secret 小程序 appSecret
     * @param js_code 登录时获取的 code -> 登录凭证校验。`前端` 通过 wx.login 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程。更多使用方法详见 小程序登录。
     * @param grant_type 授权类型，此处只需填写 authorization_code
     * @return
     */
    @Override
    public JSONObject code2Session(String appid, String secret, String js_code, String grant_type) {
        return null;
    }

    /**
     * 3.订阅消息 /send /subscribeMessage.send
     * @param access_token 接口调用凭证 -> 1.接口调用凭证 /getAccessToken /auth.getAccessToken
     * @param body 请求参数body
     * @return
     */
    @Override
    public JSONObject subscribeMessageSend(String access_token, Map<String, Object> body) {
        return null;
    }

}
