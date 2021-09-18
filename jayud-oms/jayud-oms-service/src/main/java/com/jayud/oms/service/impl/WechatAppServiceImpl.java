package com.jayud.oms.service.impl;

import cn.hutool.json.JSONObject;
import com.jayud.common.utils.HttpUtil;
import com.jayud.oms.service.WechatAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WechatAppServiceImpl implements WechatAppService {

    @Override
    public JSONObject getOpenId(String appid, String secret, String code) {
        String params = "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String s = HttpUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        JSONObject jsonObject = new JSONObject(s);
        if (jsonObject.getStr("errmsg") != null) {
            log.warn("请求小程序oppId失败 错误信息:" + jsonObject.getStr("errmsg"));
            return jsonObject;
        }
        return jsonObject;
    }
}
