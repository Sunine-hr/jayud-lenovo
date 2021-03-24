package com.jayud.finance.service;

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
    public List<AirOrderTemplate> getAirOrderTemplate(List<String> mainOrderNos);
}
