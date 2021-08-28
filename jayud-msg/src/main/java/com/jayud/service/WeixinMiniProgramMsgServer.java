package com.jayud.service;

import cn.hutool.json.JSONObject;

import java.util.Map;

/**
 * 微信小程序消息服务-订阅消息
 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/subscribe-message/subscribeMessage.send.html
 *
 */
public interface WeixinMiniProgramMsgServer {

    /**
     * 1.接口调用凭证 /getAccessToken /auth.getAccessToken
     * @param grant_type 填写 client_credential
     * @param appid 小程序唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且帐号没有异常状态）
     * @param secret 小程序唯一凭证密钥，即 AppSecret，获取方式同 appid
     * @return
     */
    JSONObject getAccessToken(String grant_type, String appid, String secret);

    /**
     * 2.登录 /code2Session /auth.code2Session
     * @param appid 小程序 appId
     * @param secret 小程序 appSecret
     * @param js_code 登录时获取的 code -> 登录凭证校验。`前端` 通过 wx.login 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程。更多使用方法详见 小程序登录。
     * @param grant_type 授权类型，此处只需填写 authorization_code
     * @return
     */
    JSONObject code2Session(String appid, String secret, String js_code, String grant_type);

    /**
     * 3.订阅消息 /send /subscribeMessage.send
     * @param access_token 接口调用凭证 -> 1.接口调用凭证 /getAccessToken /auth.getAccessToken
     * @param body 请求参数body
     * @return
     */
    JSONObject subscribeMessageSend(String access_token, Map<String, Object> body);
}
