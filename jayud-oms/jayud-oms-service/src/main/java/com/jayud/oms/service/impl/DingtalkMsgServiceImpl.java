package com.jayud.oms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.service.DingtalkMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DingtalkMsgServiceImpl implements DingtalkMsgService {

    @Autowired
    private RedisUtils redisUtils;

    /**
     获取企业凭证
     基本信息
     请求方式：GET
     请求地址：https://oapi.dingtalk.com/gettoken
     * @param appkey 应用的唯一标识key。
     * @param appsecret 应用的密钥。AppKey和AppSecret可在钉钉开发者后台的应用详情页面获取。
     * @return
     */
    @Override
    public JSONObject gettoken(String appkey, String appsecret) {
        JSONObject jsonObject = new JSONObject();
        String dingtalk_access_token = redisUtils.get("dingtalk_access_token");//钉钉-调用服务端API的应用凭证
        if(StrUtil.isNotEmpty(dingtalk_access_token)){
            jsonObject.set("errcode", 0);
            jsonObject.set("access_token", dingtalk_access_token);
            jsonObject.set("errmsg", "ok");
            return jsonObject;
        }
        HttpResponse response = HttpRequest.get("https://oapi.dingtalk.com/gettoken?appkey="+DingtalkMsgService.APPKEY+"&appsecret="+DingtalkMsgService.APPSECRET)
                .execute();
        String feedback = response.body();
        jsonObject = new JSONObject(feedback);

        Integer errcode = jsonObject.getInt("errcode");
        if(ObjectUtil.isEmpty(errcode) || !errcode.equals(0)){
            String errmsg = StrUtil.isEmpty(jsonObject.getStr("errmsg")) ? "接口调用失败，无返回值！" : jsonObject.getStr("errmsg");
            log.warn(errmsg);
            return jsonObject;
        }
        //将凭证设置在redis里面，有效期7200秒，两个小时
        dingtalk_access_token = jsonObject.getStr("access_token");
        redisUtils.set("dingtalk_access_token", dingtalk_access_token, 7200);
        return jsonObject;
    }

    /**
     手机号获取userid
     基本信息
     请求方式：GET
     请求地址：https://oapi.dingtalk.com/user/get_by_mobile
     * @param access_token 服务端API授权凭证
     * @param mobile 要获取的用户手机号。
     * @return
     */
    @Override
    public JSONObject userGetByMobile(String access_token, String mobile) {
        HttpResponse response = HttpRequest.get("https://oapi.dingtalk.com/user/get_by_mobile?access_token="+access_token+"&mobile="+mobile)
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
     发送工作通知
     基本信息
     请求方式：POST
     请求地址：https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2

     注意：
        1.同一个应用相同内容的消息，同一个用户一天只能接收一次。
        2.同一个应用给同一个用户发送消息，企业内部应用一天不得超过500次。
        3.通过设置to_all_user参数全员推送消息，一天最多3次。且企业发送消息单次最多只能给5000人发送，ISV发送消息单次最多能给1000人发送

     * @param access_token 服务端API的应用凭证
     * @param body Body参数{支持发送 文本、图片、语音、文件、链接、OA、markdown、卡片 等消息,按照格式定义}
     * @return
     */
    @Override
    public JSONObject sendMessage(String access_token, Map<String, Object> body) {

        //组装请求的参数
        JSONObject request = new JSONObject();
        request.set("agent_id", DingtalkMsgService.AGENTID);
        request.set("userid_list", body.get("userid_list"));
        request.set("msg", body.get("msg"));
        HttpResponse response = HttpRequest.post("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token="+access_token)
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
     获取工作通知消息的发送结果
     基本信息
     请求方式：POST
     请求地址：https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult

     注意：
        1.通过接口发送工作通知，当接收人列表超过100人时，不支持调用该接口，否则系统会返回调用超时。

     * @param access_token
     * @param body
     * @return
     */
    @Override
    public JSONObject getSendResult(String access_token, Map<String, Object> body) {
        //组装请求的参数
        JSONObject request = new JSONObject();
        request.set("agent_id", DingtalkMsgService.AGENTID);
        request.set("task_id", body.get("task_id"));
        HttpResponse response = HttpRequest.post("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult?access_token="+access_token)
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
     * 通过手机号给钉钉用户发送消息 (自定义的业务接口)
     * @param mobile 接收消息用户的手机号
     * @param message 发送的消息(目前仅发送文本消息，其实钉钉可以支持发送 文本、图片、语音、文件、链接、OA、markdown、卡片 等消息)
     * @return
     */
    @Override
    public JSONObject sendMessageByMobile(String mobile, String message) {
        //1.获取企业凭证,拿到token
        JSONObject gettoken = this.gettoken(DingtalkMsgService.APPKEY, DingtalkMsgService.APPSECRET);
        String access_token = gettoken.getStr("access_token");
        //2.手机号获取userid,拿到钉钉userid
        JSONObject jsonObject = this.userGetByMobile(access_token, mobile);
        String userid = jsonObject.getStr("userid");
        //3.发送工作通知,异步发送通知，拿到task_id
        Map<String, Object> body1 = new HashMap<>();
        body1.put("userid_list", userid);
        Map<String, Object> msg = new HashMap<>();
        msg.put("msgtype", "text");
        msg.put("text", new JSONObject().set("content", message+"[佳裕达,"+ DateUtils.getLocalToStr(LocalDateTime.now()) +"]"));
        body1.put("msg", msg);
        JSONObject jsonObject1 = this.sendMessage(access_token, body1);
        String task_id = jsonObject1.getStr("task_id");
        //4.获取工作通知消息的发送结果
        Map<String, Object> body2 = new HashMap<>();
        body2.put("task_id", task_id);
        JSONObject sendResult = this.getSendResult(access_token, body2);
        return sendResult;
    }
}
