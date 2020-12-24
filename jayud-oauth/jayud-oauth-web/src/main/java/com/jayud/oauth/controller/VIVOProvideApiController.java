package com.jayud.oauth.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.TokenGenerator;
import com.jayud.oauth.model.bo.BizData;
import com.jayud.oauth.model.bo.ExtAuthenticationForm;
import com.jayud.oauth.model.po.DevelopmentSetting;
import com.jayud.oauth.service.IDevelopmentSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.util.Date;


@RestController
@Api(tags = "提供给vivo调用的接口")
public class VIVOProvideApiController {

    @Autowired
    IDevelopmentSettingService settingService;

    @Autowired
    RedisUtils redisUtils;


    @ApiOperation(value = "对外授权")
    @RequestMapping("/api/extOauth")
    public CommonResult extOauth(@RequestBody ExtAuthenticationForm form) {
        String sign = form.getSign();//密文
        String appId = form.getAppId();
        String oprTime = form.getBizData().getOprTime();
        if (sign == null || "".equals(sign) || appId == null || "".equals(appId) ||
                form.getBizData() == null || "".equals(form.getBizData()) ||
                oprTime == null || "".equals(oprTime) || oprTime.length() != 14) {
            return CommonResult.error(10001, "授权请求参数不合法");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("app_id", appId);
        DevelopmentSetting developmentSettings = settingService.getOne(queryWrapper);
        if (developmentSettings == null) {
            return CommonResult.error(10002, "授权不通过");
        }
        BizData bizData = new BizData();
        bizData.setOprTime(oprTime);
        String jsonStr = JSONObject.toJSONString(bizData);
        String encodeValue = DigestUtils.md5DigestAsHex((appId + jsonStr + developmentSettings.getAppSecret()).getBytes());
        if (!encodeValue.equals(sign)) {
            return CommonResult.error(10002, "授权不通过");
        }
        String token = TokenGenerator.generateValue(encodeValue);
        redisUtils.set("token",token);
        return CommonResult.error(1,"成功",token);
    }


    public static void main(String[] args) {

        BizData bizData = new BizData();
        String date = DateUtils.format(new Date(), "YYYYMMDDHHMMSS");
        bizData.setOprTime(date);
        String jsonStr = JSONObject.toJSONString(bizData);
        String encodeValue = DigestUtils.md5DigestAsHex(("lenovo2xISJEOM" + jsonStr + "485c7a8e4a77041aeae042cd7541fb4133ed144e").getBytes());
        System.out.println(encodeValue);
        String token = TokenGenerator.generateValue(encodeValue);
        System.out.println(token);
    }


}









    



