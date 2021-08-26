package com.jayud.oms.service;

import cn.hutool.json.JSONObject;

import java.util.Map;

/**
 * 钉钉消息服务
 * 钉钉开放平台 API Explorer: https://open-dev.dingtalk.com/apiExplorer#
 *
 */
public interface DingtalkMsgService {

    /**
     * 应用凭证
     * AgentId: 1279251144
     * miniAppId: 5000000000980383
     * AppKey: ding07nvmiueit3awmod
     * AppSecret: zj4JdyA8OcvYuBf7cLTaH87S91vuMtI2PKlAtI_Ze0Q7haQ3ux6ysryhT6P-JGM4
     */
    public final static String AGENTID = "1279251144";
    public final static String MINIAPPID = "5000000000980383";
    public final static String APPKEY = "ding07nvmiueit3awmod";
    public final static String APPSECRET = "zj4JdyA8OcvYuBf7cLTaH87S91vuMtI2PKlAtI_Ze0Q7haQ3ux6ysryhT6P-JGM4";


    /**
         获取企业凭证
         请求方式：GET
         请求地址：https://oapi.dingtalk.com/gettoken
     * @param appkey 应用的唯一标识key。
     * @param appsecret 应用的密钥。AppKey和AppSecret可在钉钉开发者后台的应用详情页面获取。
     * @return access_token 服务端API授权凭证
     */
    JSONObject gettoken(String appkey, String appsecret);

    /**
         手机号获取userid
         请求方式：GET
         请求地址：https://oapi.dingtalk.com/user/get_by_mobile
     * @param access_token 服务端API授权凭证
     * @param mobile 要获取的用户手机号。
     * @return
     */
    JSONObject userGetByMobile(String access_token, String mobile);

    /**
         发送工作通知
         基本信息
         请求方式：POST
         请求地址：https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2
     * @param access_token 服务端API的应用凭证
     * @param body Body参数
     * @return
     */
    JSONObject sendMessage(String access_token, Map<String, Object> body);

    /**
         获取工作通知消息的发送结果
         基本信息
         请求方式：POST
         请求地址：https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult
     * @param access_token
     * @param body
     * @return
     */
    JSONObject getSendResult(String access_token, Map<String, Object> body);

    /**
     * 通过手机号给钉钉用户发送消息 (自定义的业务接口)
     * @param mobile 接收消息用户的手机号
     * @param message 发送的消息(文本消息，content内容，建议500字符以内。)
     * @return
     */
    JSONObject sendMessageByMobile(String mobile, String message);



}
