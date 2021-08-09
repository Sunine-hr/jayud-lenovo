package com.jayud.oms.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.RedisUtils;
import com.jayud.oms.service.WechatMsgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WechatMsgServiceImpl implements WechatMsgService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public JSONObject getEnterpriseToken(String corpid, String corpsecret, boolean enableCaching) {
        JSONObject jsonObject = new JSONObject();
        String token = redisUtils.get("Enterprise_wechat_token");
        if (StringUtils.isNotEmpty(token) && enableCaching) {
            jsonObject.put("errcode", 0);
            jsonObject.put("errmsg", "ok");
            jsonObject.put("access_token", token);
            return jsonObject;
        }

        HttpResponse response = HttpRequest.post("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret)
                .execute();
        String feedback = response.body();

        jsonObject = new JSONObject(feedback);

        if (jsonObject.getInt("errcode") != 0) {
            log.warn(jsonObject.getStr("errmsg"));
            return jsonObject;
        }
        token = jsonObject.getStr("access_token");
        redisUtils.set("Enterprise_wechat_token", token, 7200);
        return jsonObject;
    }

    @Override
    public JSONObject getEnterpriseDep(Long id, String corpid, String corpsecret, String token) {
        HttpResponse response = HttpRequest.get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + token + "&id=" + id)
                .execute();
        String feedback = response.body();

        JSONObject jsonObject = new JSONObject(feedback);

        if (jsonObject.getInt("errcode") != 0) {
            String errmsg = jsonObject.getStr("errmsg");
            log.warn(errmsg);
            if (jsonObject.getInt("errcode").equals(40014)) {
                token = this.getEnterpriseToken(corpid, corpsecret, false).getStr("access_token");
                response = HttpRequest.get("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + token + "&id=" + id)
                        .execute();
                jsonObject = new JSONObject(response.body());
            } else {
                return jsonObject;
            }
        }

        return jsonObject;
    }

    @Override
    public JSONObject getEnterpriseDepStaff(Long departmentId, boolean fetchChild, String corpid, String corpsecret, String token) {
        HttpResponse response = HttpRequest.get("https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + token + "&department_id=" + departmentId + "&fetch_child=" + fetchChild)
                .execute();
        String feedback = response.body();
        JSONObject jsonObject = new JSONObject(feedback);

        if (jsonObject.getInt("errcode") != 0) {
            String errmsg = jsonObject.getStr("errmsg");
            log.warn(errmsg);
            if (jsonObject.getInt("errcode").equals(40014)) {
                token = this.getEnterpriseToken(corpid, corpsecret, false).getStr("access_token");
                response = HttpRequest.get("https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + token + "&department_id=" + departmentId + "&fetch_child=" + fetchChild)
                        .execute();
                jsonObject = new JSONObject(response.body());
            } else {
                return jsonObject;
            }
        }
        return jsonObject;
    }

    @Override
    public JSONObject sendEnterpriseMsg(String touser, String msg, String agentid, String corpid, String corpsecret, String token) {
        JSONObject request = new JSONObject();
        request.put("touser", touser);
        request.put("msgtype", "text");
        request.put("agentid", agentid);
        request.put("text", new JSONObject().put("content", msg));
        HttpResponse response = HttpRequest.post(" https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token)
                .body(request.toString())
                .execute();
        String feedback = response.body();

        JSONObject jsonObject = new JSONObject(feedback);

        if (jsonObject.getInt("errcode") != 0) {
            String errmsg = jsonObject.getStr("errmsg");
            log.warn(errmsg);
            if (jsonObject.getInt("errcode").equals(40014)) {
                token = this.getEnterpriseToken(corpid, corpsecret, false).getStr("access_token");
                response = HttpRequest.post(" https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token)
                        .body(request.toString())
                        .execute();
                jsonObject = new JSONObject(response.body());
            } else {
                return jsonObject;
            }
        }
        return jsonObject;
    }


//    public static void main(String[] args) {
//        WechatMsgServiceImpl wechatMsgService = new WechatMsgServiceImpl();
//        String token = wechatMsgService.getEnterpriseToken("ww482dc8e849fdf6db", "fopy-oXphlFe_lsFISENSL72nje3c5eGl_tW3OvDXLM", true);
//        System.out.println(token);
//    }
}
