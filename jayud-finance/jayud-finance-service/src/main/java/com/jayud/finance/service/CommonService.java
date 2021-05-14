package com.jayud.finance.service;

import cn.hutool.json.JSONArray;
import com.jayud.finance.enums.BillTemplateEnum;
import com.jayud.finance.vo.template.order.AirOrderTemplate;

import java.util.List;
import java.util.Map;

/**
 * 公共处理类
 */
public interface CommonService {

    /**
     * 获取对应模板数据
     */
//    public Map<String,Map<String,Object>> Assembly template

    /**
     * 获取空运明细
     */
    public Map<String, Object> getAirOrderTemplate(List<String> mainOrderNos, String cmd, BillTemplateEnum templateEnum);

    /**
     * 处理模板数据
     *
     * @param cmd
     * @param array        原始数据
     * @param mainOrderNos
     * @param type         类型:应收:0,应付:1
     * @return
     */
    public JSONArray templateDataProcessing(String cmd, JSONArray array, List<String> mainOrderNos, Integer type);


    /**
     * 处理模板数据
     *
     * @param cmd
     * @param array        原始数据
     * @param mainOrderNos
     * @param type         类型:应收:0,应付:1
     * @return
     */
    public JSONArray templateDataProcessing(String cmd, String templateCmd, JSONArray array, List<String> mainOrderNos, Integer type);


    Map<String, Object> getTrailerOrderTemplate(List<String> mainOrderNos, String cmd, BillTemplateEnum templateEnum);
}
