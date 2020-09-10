package com.jayud.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.bo.*;
import com.jayud.model.vo.*;
import com.jayud.service.ICustomsApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * api接口服务实现类
 *
 * @author william
 * @description
 * @Date: 2020-09-07 15:43
 */
@Service
@Slf4j
public class ICustomsApiServiceImpl implements ICustomsApiService {
    @Autowired
    RedisUtils redisUtils;

    @Value("${yunbaoguan.urls.login}")
    String loginUrl;
    @Value("${yunbaoguan.urls.trusts}")
    String trustsUrl;
    @Value("${yunbaoguan.urls.bg-trace}")
    String bgTraceUrl;
    @Value("${yunbaoguan.urls.trust-trace}")
    String trustTraceUrl;
    @Value("${yunbaoguan.urls.declaration}")
    String declarationUrl;
    @Value("${yunbaoguan.username}")
    String defaultUserName;
    @Value("${yunbaoguan.password}")
    String defaultPassword;

    @Override
    public void login(LoginForm form) {
        doLogin(form);
    }


    @Override
    public String checkoutUserToken(LoginForm form) {
        String token = redisUtils.get(getRedisKey(form));
        if (StringUtils.isBlank(token)) {
            token = doLogin(form);
        }
        return token;
    }

    @Override
    public PushOrderVO pushOrder(PushOrderForm form) {
        String requestStr = JSONUtil.toJsonStr(form);
        //请求
        String feedback = doPost(requestStr, trustsUrl);

        PushOrderVO result = null;
        try {
            result = JSONUtil.toBean(feedback, PushOrderVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.SAVE_ERROR, "写入数据失败");
        }

        if (Objects.isNull(result)) {
            Asserts.fail(ResultEnum.SAVE_ERROR, "写入数据失败");
        }

        return result;
    }


    @Override
    public void pushAppendix(PushAppendixForm form) {
        Map<String, Object> requestParam = new HashMap<>(3);
        requestParam.put("uid", form.getUid());
        requestParam.put("fname", form.getFname());
        requestParam.put("ftype", form.getFtype());

        String feedback = doComplexPost(trustsUrl, requestParam, String.format("{\"data\": \"%s\"}", form.getData()));

        if (!StringUtils.isBlank(feedback) && !feedback.contains("上传成功")) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "上传失败");
        }
    }

    @Override
    public FindOrderInfoVO findOrderInfo(FindOrderInfoWrapperForm form) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("page", form.getPage());
        param.put("rows", form.getRows());

        FindOrderInfoForm body = ConvertUtil.convert(form, FindOrderInfoForm.class);
        String feedback = doComplexPost(trustsUrl, param, JSONUtil.toJsonStr(body));

        try {
            return JSONUtil.toBean(feedback, FindOrderInfoVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public FindOrderDetailVO findOrderDetail(String uid) {
        Map<String, Object> param = new HashMap<>(1);
        param.put("uid", uid);
        String feedback = doGet(trustsUrl, param);
        FindOrderDetailVO result;
        //如果返回参数解析失败返回null，不报错
        try {
            result = JSONUtil.toBean(feedback, FindOrderDetailVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    public DownloadCustomsDeclarationVO DownloadCustomsDeclaration(String id, String idType) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("id", id);
        param.put("idType", id);
        String feedback = doGet(declarationUrl, param);
        try {
            return JSONUtil.toBean(feedback, DownloadCustomsDeclarationVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DclarationProcessStepVO getDeclarationProcessStep(String id) {
        Map<String, Object> param = new HashMap<>(1);
        param.put("id", id);
        String feedback = doGet(bgTraceUrl, param);
        try {
            return JSONUtil.toBean(feedback, DclarationProcessStepVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OrderProcessStepVO getOrderProcessStep(String id) {
        Map<String, Object> param = new HashMap<>(1);
        param.put("id", id);
        String feedback = doGet(trustTraceUrl, param);
        try {
            return JSONUtil.toBean(feedback, OrderProcessStepVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String doGet(String url, Map<String, Object> params) {
        StringBuffer actualUrl = new StringBuffer().append(url);
        String requestUrl = url;
        if (!CollectionUtil.isEmpty(params)) {
            actualUrl.append(url + "?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                actualUrl.append(String.format("%s=%s&", entry.getKey(), entry.getValue().toString()));
            }
            requestUrl = actualUrl.toString();
            requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
        }


        return HttpRequest.get(requestUrl)
                .header("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)))
                .execute()
                .body();
    }

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
        if (StringUtils.isBlank(ticket)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "登录失败，用户名或密码错误");
        }
        //token不为空，放入redis，过期时间12小时
        redisUtils.set(getRedisKey(form), ticket, RedisUtils.EXPIRE_YUNBAOGUAN);
        return ticket;
    }

    private String doPost(String requestStr, String url) {
        return HttpRequest
                .post(url)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)))
                .body(requestStr)
                .execute().body();
    }

    private String doComplexPost(String url, Map<String, Object> requestParams, String body) {
        //拼接路径参数
        StringBuffer urlBase = new StringBuffer().append(String.format("%s?", url));
        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            urlBase.append(String.format("%s=%s&", entry.getKey(), entry.getValue().toString()));
        }
        String paramedUrl = urlBase.toString();

        return
                HttpRequest
                        .post(paramedUrl.substring(0, paramedUrl.length() - 1))
                        .header("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)))
                        .body(body)
                        .execute()
                        .body();
    }

    private String getRedisKey(LoginForm form) {
        return form.getName() + form.getPassword();
    }
}
