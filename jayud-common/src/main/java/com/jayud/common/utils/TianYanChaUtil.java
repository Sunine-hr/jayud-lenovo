package com.jayud.common.utils;


import cn.hutool.json.JSONObject;
import com.jayud.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @anthor Satellite
 * TianYanChaUtil
 * 天眼查工具类
 * http://www.javalow.com
 * @date 2018-04-29-17:13
 **/
@Component
public class TianYanChaUtil {

    @Autowired
    private Environment env;

    private static String token;
    private static String queryEnterpriseUrl;
    private static String fuzzyUrl;
    public static final Integer RESULT_CODE = 0;

    @PostConstruct
    public void readConfig() {
        token = env.getProperty("tianyancha.token");
        queryEnterpriseUrl = env.getProperty("tianyancha.precisev3.url");
        fuzzyUrl = env.getProperty("tianyancha.fuzzy.url");
    }


    public static Result tianYanChaGet(String url, Map<String, String> params) throws Exception {
        //设置请求头
        try {
            Map<String, String> propertys = new HashMap<>();
            propertys.put("Authorization", token);
            HttpRequester requester = new HttpRequester();
            JSONObject json = requester.sendGet(url, params, propertys);
            if (Integer.parseInt(json.get("error_code").toString()) == RESULT_CODE) {
                return Result.ok().put("data", json.get("result"));
            } else {
                return Result.error();
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * 精准查询天眼查信息
     *
     * @param name
     * @return
     * @throws Exception
     */
    public static Result fuzzyQuery2Enterprise(String name) throws Exception {
        Map map = new HashMap();
        map.put("name", name);
        return TianYanChaUtil.tianYanChaGet(queryEnterpriseUrl, map);
    }

    /**
     * 模糊查询企业列表
     *
     * @param name
     * @return
     * @throws Exception
     */
    public static Result queryEnterpriseList(String name) throws Exception {
        Map map = new HashMap();
        map.put("word", name);
        return TianYanChaUtil.tianYanChaGet(fuzzyUrl, map);
    }
}
