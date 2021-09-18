package com.jayud.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.jayud.common.RedisUtils;
import com.jayud.service.WeixinMiniProgramMsgServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WeixinMiniProgramMsgServerImpl implements WeixinMiniProgramMsgServer {

    /**
     * 微信-小程序-佳裕达综合物流
     * */
    @Value("${weixin.miniprogram.appid}")
    private String appid;
    @Value("${weixin.miniprogram.appsecret}")
    private String appsecret;
    //订阅消息-模板id
    @Value("${weixin.miniprogram.templatelist.templateid01}")
    private String templateid01;
    @Value("${weixin.miniprogram.templatelist.templateid02}")
    private String templateid02;
    @Value("${weixin.miniprogram.templatelist.templateid03}")
    private String templateid03;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 1.接口调用凭证 /getAccessToken /auth.getAccessToken
     * @param grant_type 填写 client_credential
     * @param appid 小程序唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且帐号没有异常状态）
     * @param secret 小程序唯一凭证密钥，即 AppSecret，获取方式同 appid
     * @return
     */
    @Override
    public JSONObject getAccessToken(String grant_type, String appid, String secret) {

        JSONObject jsonObject = new JSONObject();
        String key = "weixin_miniprogram_access_token"+"_"+appid;
        String weixin_miniprogram_access_token = redisUtils.get(key);//钉钉-调用服务端API的应用凭证
        if(StrUtil.isNotEmpty(weixin_miniprogram_access_token)){
            jsonObject.set("errcode", 0);
            jsonObject.set("access_token", weixin_miniprogram_access_token);
            jsonObject.set("errmsg", "ok");
            return jsonObject;
        }
        HttpResponse response = HttpRequest.get("https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+appid+"&secret="+secret+"")
                .execute();
        String feedback = response.body();
        jsonObject = new JSONObject(feedback);

        Integer errcode = jsonObject.getInt("errcode");
        if(ObjectUtil.isNotEmpty(errcode) && !errcode.equals(0)){
            String errmsg = jsonObject.getStr("errmsg");
            log.warn(errmsg);
            return jsonObject;
        }
        //将凭证设置在redis里面，有效期7200秒，两个小时
        weixin_miniprogram_access_token = jsonObject.getStr("access_token");
        redisUtils.set(key, weixin_miniprogram_access_token, 7200);
        return jsonObject;
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
        HttpResponse response = HttpRequest.get("https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type="+grant_type+"")
                .execute();
        String feedback = response.body();
        JSONObject jsonObject = new JSONObject(feedback);
        Integer errcode = jsonObject.getInt("errcode");
        if(ObjectUtil.isEmpty(errcode) || !errcode.equals(0)){
            String errmsg = StrUtil.isEmpty(jsonObject.getStr("errmsg")) ? "接口调用失败，无返回值！" : jsonObject.getStr("errmsg");
            log.warn(errmsg);
        }
        return jsonObject;
    }

    /**
     * 3.订阅消息 /send /subscribeMessage.send
     * @param access_token 接口调用凭证 -> 1.接口调用凭证 /getAccessToken /auth.getAccessToken
     * @param body 请求参数body
     * @return
     */
    @Override
    public JSONObject subscribeMessageSend(String access_token, Map<String, Object> body) {
        //组装请求的参数
        JSONObject request = new JSONObject();
        request.set("touser", body.get("touser"));//接收者（用户）的 openid，必填
        request.set("template_id", body.get("template_id"));//所需下发的订阅模板id，必填
        request.set("data", body.get("data"));//模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }，必填
        if(ObjectUtil.isNotEmpty(body.get("page"))){
            request.set("page", body.get("page"));//点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
        }
        if(ObjectUtil.isNotEmpty(body.get("miniprogram_state"))){
            request.set("miniprogram_state", body.get("miniprogram_state"));//跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
        }
        if(ObjectUtil.isNotEmpty(body.get("lang"))){
            request.set("lang", body.get("lang"));//进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
        }
        HttpResponse response = HttpRequest.post("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+access_token+"")
                .body(request.toString())
                .execute();
        String feedback = response.body();
        JSONObject jsonObject = new JSONObject(feedback);
        Integer errcode = jsonObject.getInt("errcode");
        if(ObjectUtil.isEmpty(errcode) || !errcode.equals(0)){
            String errmsg = StrUtil.isEmpty(jsonObject.getStr("errmsg")) ? "接口调用失败，无返回值！" : jsonObject.getStr("errmsg");
            log.warn(errmsg);
        }
        return jsonObject;
    }

    /**
     * 通过openid，给微信小程序用户发送订阅消息
     * @param openid 接收者（用户）的 openid
     * @param body
     * @return
     */
    @Override
    public JSONObject sendMessageByOpenid(String openid, Map<String, Object> body) {
        //1.接口调用凭证，获取token
        JSONObject jsonObject1 = this.getAccessToken("client_credential", appid, appsecret);
        String access_token = jsonObject1.getStr("access_token");
        Integer errcode1 = jsonObject1.getInt("errcode");
        if(ObjectUtil.isNotEmpty(errcode1) && !errcode1.equals(0)){
            String errmsg = jsonObject1.getStr("errmsg");
            log.warn(errmsg);
            jsonObject1.set("errmsg", errmsg);
            return jsonObject1;
        }
        //2.订阅消息，发送
        Map<String, Object> body2 = new HashMap<>();
        body2.put("touser", openid);//接收者（用户）的 openid
        body2.put("template_id", body.get("template_id"));
        body2.put("data", body.get("data"));

        body2.put("page", body.get("page"));
        body2.put("miniprogram_state", body.get("miniprogram_state"));
        body2.put("lang", body.get("lang"));

        JSONObject jsonObject2 = this.subscribeMessageSend(access_token, body2);
        Integer errcode2 = jsonObject2.getInt("errcode");
        if(ObjectUtil.isEmpty(errcode2) || !errcode2.equals(0)){
            String errmsg = StrUtil.isEmpty(jsonObject2.getStr("errmsg")) ? "接口调用失败，无返回值！" : jsonObject2.getStr("errmsg");
            log.warn(errmsg);
            jsonObject2.set("errmsg", errmsg);
            return jsonObject2;
        }
        return jsonObject2;
    }


}
