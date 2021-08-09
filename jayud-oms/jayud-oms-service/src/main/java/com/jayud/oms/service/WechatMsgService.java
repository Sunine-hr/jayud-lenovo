package com.jayud.oms.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.util.Map;

public interface WechatMsgService {

    public JSONObject getEnterpriseToken(String corpid, String corpsecret, boolean enableCaching);

    public JSONObject getEnterpriseDep(Long id, String corpid, String corpsecret, String token);

    public JSONObject getEnterpriseDepStaff(Long departmentId, boolean fetchChild, String corpid, String corpsecret, String token);

    public JSONObject sendEnterpriseMsg(String touser, String msg, String agentid, String corpid, String corpsecret, String token);
}
