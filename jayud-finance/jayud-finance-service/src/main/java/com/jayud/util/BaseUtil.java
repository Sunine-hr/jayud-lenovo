package com.jayud.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayud.kingdeesettings.K3CloudConfigBase;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2019/1/7 15:58
 */
public class BaseUtil {

    private BaseUtil() {
    }

    ;

    /**
     * 构造登录参数
     *
     * @param config 配置对象
     * @param lang   语言
     * @return
     */
    public static String buildLogin(K3CloudConfigBase config, int lang) {
        Map<String, Object> param = new HashMap<>();
        param.put("acctID", config.getDbId());
        param.put("username", config.getUserName());
        param.put("password", config.getPasswd());
        param.put("lcid", lang);
        return JSON.toJSONString(param);
    }


    /**
     * 初始化物料信息
     *
     * @param template 物料基础数据模板
     * @param formid   表单id
     * @param code     物料编码
     * @param name     物料名称
     * @param attr     物料规格属性
     * @return
     */
    public static String buildMaterial(String template, String formid, String code, String name, String attr) {
        JSONObject basic = JSON.parseObject(template);
        Map<String, Object> model = (Map<String, Object>) basic.get("Model");
        model.put("FNumber", code);
        model.put("FName", name);
        model.put("FSpecification", attr);
        basic.put("Model", model);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formid", formid);
        jsonObject.put("data", JSON.toJSONString(basic));
        return JSON.toJSONString(jsonObject);
    }

    /**
     * 构造提交、审核参数
     *
     * @param formid  表单id
     * @param numbers 编码 多个编码以,分隔
     * @param flags   审核标示 多个以,分隔 和编码一一对应
     * @return
     */
    public static String buildParam(String formid, String numbers, String flags) {
        JSONObject jsonObject = new JSONObject();
        JSONObject param = new JSONObject();
        if (flags != null) {
            String[] arr_flag = flags.split(",");
            param.put("InterationFlags", arr_flag);
        }
        String[] arr_number = numbers.split(",");
        param.put("Numbers", arr_number);
        jsonObject.put("formid", formid);
        jsonObject.put("data", JSON.toJSONString(param));
        return JSON.toJSONString(jsonObject);
    }

}