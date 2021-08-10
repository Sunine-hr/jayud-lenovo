package com.jayud.oms.service;

import cn.hutool.json.JSONObject;

public interface WechatAppService {

    public JSONObject getOpenId(String appid, String secret, String code);
}
