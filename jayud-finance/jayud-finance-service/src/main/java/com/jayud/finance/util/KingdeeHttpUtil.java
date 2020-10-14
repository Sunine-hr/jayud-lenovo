package com.jayud.finance.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 金蝶http请求工具类；
 * <p>
 * TODO 网上copy下来的，需要继续抽象，抽象到公共的httputil去
 * </p>
 */
@Slf4j
public class KingdeeHttpUtil {

    private KingdeeHttpUtil() {

    }

    /**
     * 连接超时时间
     */
    private static final int CONN_TIMEOUT = 30000;
    /**
     * 请求超时时间
     */
    private static final int READ_TIMEOUT = 30000;

    private static RestTemplate restTemplate = null;

    static {
        //设置超时时间
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONN_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        restTemplate = new RestTemplate(requestFactory);
    }


    /**
     * 发送Get请求
     *
     * @param url     请求地址
     * @param headers 请求头参数
     * @return
     */
    public static String httpGet(String url, Map<String, Object> headers) {

        //初始化header公共参数
        HttpHeaders httpHeaders = initHeader();
        //设置header参数
        setHeaderParam(httpHeaders, headers);
        //发送请求
        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return responseEntity.getBody();

    }


    /**
     * 发送Get请求
     *
     * @param url     请求地址
     * @param headers 请求头参数
     * @param param   查询参数
     * @return
     */
    public static String httpGet(String url, Map<String, Object> headers, Map<String, Object> param) {

        //如果查询参数为空，则调用不带参数的Get请求
        if (CollectionUtils.isEmpty(param)) {
            return httpGet(url, headers);
        }

        //组装查询参数
        Set<String> keys = param.keySet();
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for (String key : keys) {
            builder.append(key).append("=").append(param.get(key)).append("&");
        }
        builder.deleteCharAt(builder.lastIndexOf("&"));
        url = url + builder.toString();
        //发送请求
        return httpGet(url, headers);

    }


    /**
     * 发送Post请求
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return
     */
    public static String httpPost(String url, Map<String, Object> param) {

        //初始化header公共参数
        HttpHeaders httpHeaders = initHeader();
        //设置参数
        MultiValueMap<String, Object> valueMap = setParam(param);
        //发送请求
        return toPost(url, httpHeaders, valueMap);
    }

    /**
     * 发送Post请求
     *
     * @param url  请求地址
     * @param json json格式参数参数
     * @return
     */
    public static ResponseEntity<String> httpPost(String url, String json) {

        //初始化header公共参数
        HttpHeaders httpHeaders = initHeader();
        //发送请求
        return toPost(url, httpHeaders, json);
    }

    /**
     * 发送Post请求
     *
     * @param url  请求地址
     * @param json json格式参数参数
     * @return
     */
    public static String httpPost(String url, Map<String, Object> header, String json) {
        log.debug("请求json={}", json);
        //初始化header公共参数
        HttpHeaders httpHeaders = initHeader();
        setHeaderParam(httpHeaders, header);
        //发送请求
        return toPost(url, httpHeaders, json).getBody();
    }


    /**
     * 发送POST请求
     *
     * @param url     请求地址
     * @param headers 请求头参数
     * @param param   查询参数
     * @return
     */
    public static String httpPost(String url, Map<String, Object> headers, Map<String, Object> param) {

        //初始化header公共参数
        HttpHeaders httpHeaders = initHeader();
        //设置header参数
        setHeaderParam(httpHeaders, headers);
        //设置参数
        MultiValueMap<String, Object> valueMap = setParam(param);
        return toPost(url, httpHeaders, valueMap);

    }

    /**
     * 解析金蝶响应体返回是否处理成功
     * <br>如果金蝶响应为成功，那么将返回
     * <br>如果金蝶响应为失败，那么打印错误日志并返回false
     *
     * @return
     */
    public static Boolean ifSucceed(String responseJsonStr) {
        String errorCode = findByTargetFromJson(responseJsonStr, "IsSuccess");
        String message = findByTargetFromJson(responseJsonStr, "Message");
        if (Boolean.valueOf(errorCode) == true) {
            return true;
        }
        log.error(message);
        return false;
    }

    /**
     * 删除订单时使用，返回报错信息中能够匹配入参列表的订单号，即返回操作异常的单号列表
     *
     * @param responseJsonStr
     * @param targetOrders
     * @return
     */
    public static List<String> ifSucceed(String responseJsonStr, List<String> targetOrders) {
        String errorCode = findByTargetFromJson(responseJsonStr, "IsSuccess");
        String message = findByTargetFromJson(responseJsonStr, "Message");
        if (Boolean.valueOf(errorCode) == true) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        for (String targetOrder : targetOrders) {
            if (message.contains(targetOrder)) {
                result.add(targetOrder);
            }
        }
        return result;
    }

    private static String findByTargetFromJson(String jsonStr, String target) {
        StringBuilder result = new StringBuilder();
        extractJsonAndGetObject(JSONObject.parseObject(jsonStr), target, result);
        if (StringUtils.isNotEmpty(result.toString())) {
            return result.toString();
        }
        return null;
    }

    private static void extractJsonAndGetObject(JSONObject data, String target, StringBuilder result) {
        if (CollectionUtil.isNotEmpty(data)) {
            for (String key : data.keySet()) {
                if (key.equals(target)) {
                    result.append(data.get(key).toString());
                } else {
                    extractFromObject(target, result, data.get(key));
                }
            }
        }
    }

    private static void extractFromObject(String target, StringBuilder result, Object object) {
        if (Objects.nonNull(object)) {
            if (JSONObject.class.isAssignableFrom(object.getClass())) {
                extractJsonAndGetObject((JSONObject) object, target, result);
            }
            if (JSONArray.class.isAssignableFrom(object.getClass())) {
                JSONArray subList = (JSONArray) object;
                if (CollectionUtil.isNotEmpty(subList)) {
                    for (Object o : subList) {
                        extractFromObject(target, result, o);
                    }
                }
            }
        }
    }

    /**
     * 发送请求
     *
     * @param valueMap
     * @param httpHeaders
     * @param url
     * @return
     */
    private static String toPost(String url, HttpHeaders httpHeaders, MultiValueMap<String, Object> valueMap) {
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(valueMap, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        return responseEntity.getBody();
    }

    /**
     * 发送请求
     *
     * @param json
     * @param httpHeaders
     * @param url
     * @return
     */
    private static ResponseEntity<String> toPost(String url, HttpHeaders httpHeaders, String json) {
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        return responseEntity;
    }

    /**
     * 设置header公共参数
     */
    private static HttpHeaders initHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Accpet-Encoding", "gzip");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        return headers;
    }

    /**
     * 设置header参数
     *
     * @param httpHeaders
     * @param headers
     */
    private static void setHeaderParam(HttpHeaders httpHeaders, Map<String, Object> headers) {
        if (!CollectionUtils.isEmpty(headers)) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                httpHeaders.add(key, headers.get(key).toString());
            }
        }

    }

    /**
     * 设置参数
     *
     * @param param
     * @return
     */
    private static MultiValueMap<String, Object> setParam(Map<String, Object> param) {
        MultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
        if (CollectionUtils.isEmpty(param)) {
            return valueMap;
        }
        Set<String> paramKeys = param.keySet();
        for (String key : paramKeys) {
            valueMap.add(key, param.get(key).toString());
        }
        return valueMap;
    }


}