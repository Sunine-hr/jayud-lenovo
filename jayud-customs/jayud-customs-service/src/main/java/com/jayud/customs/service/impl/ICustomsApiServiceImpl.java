package com.jayud.customs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.Gson;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.HttpRequester;
import com.jayud.common.utils.RsaEncryptUtil;
import com.jayud.customs.feign.FinanceClient;
import com.jayud.customs.feign.MsgClient;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.po.CustomsPayable;
import com.jayud.customs.model.po.YunbaoguanReceivableCost;
import com.jayud.customs.model.vo.*;
import com.jayud.customs.service.ICustomsApiService;
import com.jayud.customs.service.IYunbaoguanReceivableCostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    FinanceClient financeClient;

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

    @Autowired
    IYunbaoguanReceivableCostService yunbaoguanReceivableCostService;


    @Override
    public void login(LoginForm form) {
        doLogin(form);
    }

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


    @Override
    public String checkoutUserToken(LoginForm form) {
        String token = redisUtils.get(getRedisKey(form));
        if (StringUtils.isEmpty(token)) {
            token = doLogin(form);
        }
        return token;
    }

    //委托单上传
    @Override
    public PushOrderVO pushOrder(PushOrderForm form) {
        Gson gson = new Gson();
        String requestStr = gson.toJson(form);
        System.out.println("requestStr=================================="+requestStr);
        //请求
        String feedback = doPost(requestStr, trustsUrl);
        System.out.println("feedback====================================="+feedback);

        PushOrderVO result = null;
        try {
            result = JSONUtil.toBean(feedback, PushOrderVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.SAVE_ERROR, "出现异常，写入数据失败");
        }

        if (Objects.isNull(result)) {
            Asserts.fail(ResultEnum.SAVE_ERROR, "返回值为null,写入数据失败");
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

        log.debug(String.format("拼装数据完成，发送数据请求云报关..."));
        String receivable = doPost(JSONUtil.toJsonStr(recParam), financeUrl);
        String payable = doPost(JSONUtil.toJsonStr(payParam), financeUrl);

        log.info(String.format("应收账款receivable..."));
        log.info(receivable);
        log.info(String.format("应付款payable..."));
        log.info(payable);

//        log.info(String.format("拼装数据完成，开始请求金蝶接口..." + receivable + "====" + receivable));
        //应付为行显示费用
        //应收为列显示费用，但可能存在多行应收对应同一个报关单
        Boolean hasReceivable = false;
        Boolean hasPayable = false;
        JSONArray receivableArray = new JSONArray();
        //暂不修改应收单的实体收发
        List<CustomsPayable> payableArray = new ArrayList<>();

        //云报关应付单的数据结构不允许使用实体进行收发，需要利用数据表和map进行匹配
        try {
            receivableArray = JSONArray.parseArray(receivable);
            if (!CollectionUtil.isEmpty(receivableArray)) {
                hasReceivable = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            payableArray = JSONArray.parseArray(payable, CustomsPayable.class);
            if (!CollectionUtil.isEmpty(payableArray)) {
                hasPayable = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean sentReceivableStatus = true;
        Boolean sentPayableStatus = true;
        if (hasPayable || hasReceivable) {
            String customerName = "";
            if (hasReceivable) {
                //应收单会出现一个报关单号对应多个行的情况，一般是因为柜号不一致，要区分计费
                sentReceivableStatus = generateKafkaMsg("financeTest", "customs-receivable", receivable);

                // 保存云报关应收费用
                YunbaoguanReceivableCost receivableCost = yunbaoguanReceivableCostService.getOne(
                        Wrappers.<YunbaoguanReceivableCost>lambdaQuery()
                                .eq(YunbaoguanReceivableCost::getApplyNo, form.getApplyNo()));
                YunbaoguanReceivableCost yunbaoguanReceivableCost = new YunbaoguanReceivableCost();
                if (Objects.isNull(receivableCost)) {
                    yunbaoguanReceivableCost.setApplyNo(form.getApplyNo());
                    yunbaoguanReceivableCost.setUid(form.getUid());
                    yunbaoguanReceivableCost.setReceivableCostData(JSONUtil.toJsonStr(receivable));
                    yunbaoguanReceivableCost.setCreatedTime(LocalDateTime.now());
                    yunbaoguanReceivableCost.setUpdatedTime(LocalDateTime.now());
                } else {
                    receivableCost.setReceivableCostData(JSONUtil.toJsonStr(receivable));
                    receivableCost.setUpdatedTime(LocalDateTime.now());
                    yunbaoguanReceivableCost = receivableCost;
                }
                yunbaoguanReceivableCostService.saveOrUpdate(yunbaoguanReceivableCost);
            } else {
                //如果本次推送没有应收数据，需要查看是否存在本单号的应收，如有，要删去
                Map<String, String> map = new HashMap<>();
                map.put("applyNo", form.getApplyNo());
                Boolean aBoolean = financeClient.checkNRemoveReceivable(map);
                if (aBoolean) {
                    log.debug("报关单号 {} 没有应收数据，清理成功");
                }
            }
            if (hasPayable) {
                log.debug(String.format("拼装数据完成，开始上传财务数据：customs-payable..." + payable.replaceAll("%", "%%") + "====" + payable.replaceAll("%", "%%")));
                sentPayableStatus = generateKafkaMsg("financeTest", "customs-payable", payable);
            } else {
                //如果本次推送没有应付数据，需要查看是否存在本单号的应付，如有，要删去
                Map<String, String> map = new HashMap<>();
                map.put("applyNo", form.getApplyNo());
                Boolean aBoolean = financeClient.checkNRemovePayable(map);
                if (aBoolean) {
                    log.debug("报关单号 {} 没有应付数据，清理成功");
                }
            }

            if (!sentPayableStatus || !sentReceivableStatus) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "发送金蝶失败");
            }
        } else {
            if (!sentPayableStatus || !sentReceivableStatus) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "发送金蝶失败");
            }
        }
    }


    private Boolean generateKafkaMsg(String topic, String key, String msg) {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("topic", topic);
        msgMap.put("key", key);
        msgMap.put("msg", JSONUtil.toJsonStr(msg));
        log.debug(String.format("开始发送数据给kafka..."));
        Map<String, String> consume = msgClient.consume(msgMap);

        if (Objects.nonNull(consume)) {
            log.debug(consume.toString());
            String code = MapUtil.getStr(consume, "code");
            String errorMsg = MapUtil.getStr(consume, "msg");
            String data = MapUtil.getStr(consume, "data");
            if (StringUtils.isNotEmpty(code)) {
                if (Integer.parseInt(code) == ResultEnum.SUCCESS.getCode()) {
                    return true;
                }
            } else {
                return false;
            }
        }
//        if (consume.getCode() != ResultEnum.SUCCESS.getCode()) {
//            //调用失败将返回false
//            return false;
//        }
        return true;
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

    private String doPost(String requestStr, String url) {
//        RestTemplate client = new RestTemplate();
//        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        String feedback = HttpRequest
                .post(url)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)))
                .body(JSONUtil.toJsonStr(requestStr))
                .execute().body();

//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        headers.add("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)));
//
//        HttpEntity<Map<String, String>> requestEntity = null;
//        requestEntity = new HttpEntity<Map<String, String>>(JSONUtil.toBean(requestStr, Map.class), headers);
//        //  执行HTTP请求
//        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
//        return response.getBody();
        return feedback;

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
