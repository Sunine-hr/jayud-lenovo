package com.jayud.scm.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.scm.model.bo.LoginForm;
import com.jayud.scm.service.IHgTruckApiService;
import com.jayud.scm.service.IHgTruckService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 *
 * </p>
 *
 * @author LLJ
 * @since 2021-09-27
 */
@Service
public class HgTruckApiServiceImpl implements IHgTruckApiService {

    @Autowired
    private RedisUtils redisUtils;

//    @Value("${yunbaoguan.urls.login}")
    private String loginUrl;

    //登录
    private String doLogin(LoginForm form) {
        //入参键值对
        Map<String, String> requestMap = new HashMap<>(2);
        requestMap.put("name", form.getName());
        requestMap.put("password", form.getPassword());

        //请求
        String feedback = HttpRequest
                .post(loginUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(JSONUtil.toJsonStr(requestMap))
                .execute().body();
        //获取token
        Map map = JSONUtil.toBean(feedback, Map.class);
        String ticket = MapUtil.getStr(map, "ticket");
        if (StringUtils.isEmpty(ticket)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "登录失败，用户名或密码错误");
        }
        //token不为空，放入redis，过期时间12小时
        redisUtils.set(getRedisKey(form), ticket, RedisUtils.EXPIRE_YUNBAOGUAN);
        return ticket;
    }

    private String getRedisKey(LoginForm form) {
        return form.getName() + form.getPassword();
    }
}
