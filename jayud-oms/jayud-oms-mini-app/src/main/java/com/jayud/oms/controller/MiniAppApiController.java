package com.jayud.oms.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.utils.HttpUtil;
import com.jayud.common.utils.WXBizDataCrypt;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/miniAppApi")
@Slf4j
@Api(tags = "微信小程序api")
public class MiniAppApiController {

    // 小程序 AppID
    private static final String appid = "xxxxxxxxxxxxxxxx";
    // 小程序 AppSecret
    private static final String secret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    /**
     * 小程序授权登录
     */
    @PostMapping(value = "/miniLogin")
    public Map<String, Object> miniLogin(@RequestBody Map<String, Object> paramMap) {
        String code = MapUtil.getStr(paramMap, "code");
        String encryptedData = MapUtil.getStr(paramMap, "encryptedData");
        String iv = MapUtil.getStr(paramMap, "iv");
        Map<String, Object> map = new HashMap<>();

        String params = "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String s = HttpUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        JSONObject jsonObject = JSONObject.parseObject(s);

        String session_key = jsonObject.getString("session_key");
        String openid = jsonObject.getString("openid");
        String unionid = jsonObject.getString("unionid");

        String result = WXBizDataCrypt.decrypt1(encryptedData, session_key, iv);
        JSONObject json = JSONObject.parseObject(result);

        if (!StringUtils.isEmpty(result) && result.length() > 0) {
            if (unionid != null) {
                map.put("unionid", unionid);
            } else {
                map.put("unionid", null);
            }
            map.put("session_key", session_key);
            map.put("openid", openid);
            map.put("msg", "success");
            return map;
        }
        map.put("msg", "error");
        return map;
    }

}
