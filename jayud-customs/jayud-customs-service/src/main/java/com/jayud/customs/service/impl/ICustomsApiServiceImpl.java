package com.jayud.customs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.HttpRequester;
import com.jayud.customs.feign.MsgClient;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.vo.*;
import com.jayud.customs.service.ICustomsApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.io.*;
import java.util.HashMap;
import java.util.List;
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
    private RedisUtils redisUtils;

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
    @Value("${yunbaoguan.urls.finance}")
    String financeUrl;
    @Value("${yunbaoguan.username}")
    String defaultUserName;
    @Value("${yunbaoguan.password}")
    String defaultPassword;

    @Autowired
    MsgClient msgClient;


    @Override
    public void login(LoginForm form) {
        doLogin(form);
    }


    @Override
    public String checkoutUserToken(LoginForm form) {
        String token = redisUtils.get(getRedisKey(form));
        if (StringUtils.isEmpty(token)) {
            token = doLogin(form);
        }
        return token;
    }

    @Override
    public PushOrderVO pushOrder(PushOrderForm form) {
        Gson gson = new Gson();
        String requestStr =gson.toJson(form);
        //请求
        JSONObject feedback = doPost(requestStr, trustsUrl);

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

        JSONObject feedback = doComplexPost(trustsUrl, requestParam, String.format("{\"data\": \"%s\"}", form.getData()));

        String jsonStr = JSONUtil.toJsonStr(feedback);
        if (!StringUtils.isEmpty(jsonStr) && !jsonStr.contains("上传成功")) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "上传失败");
        }
    }

    @Override
    public FindOrderInfoVO findOrderInfo(FindOrderInfoWrapperForm form) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("page", form.getPage());
        param.put("rows", form.getRows());

        Gson gson = new Gson();

        FindOrderInfoForm body = ConvertUtil.convert(form, FindOrderInfoForm.class);
        JSONObject feedback = doComplexPost(trustsUrl, param, gson.toJson(body));


        try {
            if (null == feedback) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "接口请求失败");
            }
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
        JSONObject feedback = doGet(trustsUrl, param);
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
        param.put("idType", idType);
        JSONObject feedback = doGet(declarationUrl, param);
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
        JSONObject feedback = doGet(bgTraceUrl, param);
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
        JSONObject feedback = doGet(trustTraceUrl, param);
        try {
            return JSONUtil.toBean(feedback, OrderProcessStepVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void getFinanceInfoAndPush2Kingdee(GetFinanceInfoForm form) {
        Map<String, String> recParam = new HashMap<>();
        Map<String, String> payParam = new HashMap<>();

        if (StringUtils.isNotBlank(form.getApplyNo())) {
            recParam.put("apply_no", form.getApplyNo());
            payParam.put("apply_no", form.getApplyNo());
        }
        if (StringUtils.isNotBlank(form.getUnifyNo())) {
            recParam.put("unify_no", form.getUnifyNo());
            payParam.put("unify_no", form.getUnifyNo());
        }
        if (StringUtils.isNotBlank(form.getTrustId())) {
            recParam.put("trust_id", form.getTrustId());
            payParam.put("trust_id", form.getTrustId());
        }
        if (StringUtils.isNotBlank(form.getId())) {
            recParam.put("id", form.getId());
            payParam.put("id", form.getId());
        }
        recParam.put("costtype", "1");
        payParam.put("costtype", "2");

        JSONObject receivable = doPost(JSONUtil.toJsonStr(recParam), financeUrl);
        JSONObject payable = doPost(JSONUtil.toJsonStr(payParam), financeUrl);


        try {
            generateKafkaMsg("finance", "customs-receivable", receivable);
            generateKafkaMsg("finance", "customs-receivable", payable);
        } catch (Exception e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.PARAM_ERROR, "发送金蝶失败");
        }
    }

    private void generateKafkaMsg(String topic, String key, JSONObject msg) {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("topic", topic);
        msgMap.put("key", key);
        msgMap.put("msg", JSONUtil.toJsonStr(msg));
        msgClient.sendMessage(msgMap);
    }

    private JSONObject doGet(String url, Map<String, Object> params) {
        StringBuffer actualUrl = new StringBuffer().append(url);
        String requestUrl = url;
        if (!CollectionUtil.isEmpty(params)) {
            actualUrl.append("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                actualUrl.append(String.format("%s=%s&", entry.getKey(), entry.getValue().toString()));
            }
            requestUrl = actualUrl.toString();
            requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
        }
        HttpRequester httpRequester = new HttpRequester();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)));
        try {
            JSONObject jsonObject = httpRequester.sendGet(requestUrl, null, headerMap);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//todo 为什么返回的数据不是json
//        return HttpRequest.get(requestUrl)
//                .header("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)))
//                .execute()
//                .body();
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
        if (StringUtils.isEmpty(ticket)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "登录失败，用户名或密码错误");
        }
        //token不为空，放入redis，过期时间12小时
        redisUtils.set(getRedisKey(form), ticket, RedisUtils.EXPIRE_YUNBAOGUAN);
        return ticket;
    }

    private JSONObject doPost(String requestStr, String url) {
        HttpRequester httpRequester = new HttpRequester();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put(Header.CONTENT_TYPE.name(), "application/json");
        headerMap.put("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)));


        JSONObject jsonObject = null;
        try {
            jsonObject = httpRequester.sendPost(url, JSONUtil.toBean(requestStr, Map.class), headerMap);
        } catch (Exception e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.PARAM_ERROR, "接口请求失败");
        }
        return jsonObject;
//        return HttpRequest
//                .post(url)
//                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .header("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)))
//                .body(requestStr)
//                .execute().body();
    }

    private JSONObject doComplexPost(String url, Map<String, Object> requestParams, String body) {
        //拼接路径参数
        StringBuffer urlBase = new StringBuffer().append(String.format("%s?", url));
        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            urlBase.append(String.format("%s=%s&", entry.getKey() == null ? "" : entry.getKey(),
                    entry.getValue().toString() == null ? "" : entry.getValue()));
        }
        String paramedUrl = urlBase.toString().substring(0, urlBase.toString().length() - 1);


        HttpRequester httpRequester = new HttpRequester();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put(Header.CONTENT_TYPE.name(), "application/json");
        headerMap.put("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)));


        JSONObject jsonObject = null;
        try {
            jsonObject = httpRequester.sendPost(paramedUrl, null, headerMap);
        } catch (Exception e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.PARAM_ERROR, "接口请求失败");
        }
        return jsonObject;

    }

    private String getRedisKey(LoginForm form) {
        return form.getName() + form.getPassword();
    }
}
