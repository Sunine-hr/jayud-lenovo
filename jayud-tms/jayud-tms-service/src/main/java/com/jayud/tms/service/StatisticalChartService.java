package com.jayud.tms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.tms.model.bo.BasePageForm;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.vo.statistical.BusinessPeople;
import com.jayud.tms.model.vo.statistical.ProcessNode;
import com.jayud.tms.model.vo.statistical.TVOrderTransportVO;

import java.util.List;
import java.util.Map;

public interface StatisticalChartService {

    /**
     *
     * @param form
     * @return
     */
    public IPage<TVOrderTransportVO> findTVShowOrderByPage(BasePageForm form, List<String> legalNames);

    public Map<Long, List<ProcessNode>> processNodeProcessing(Map<Long, String> subOrderMaps);


    /**
     * 业务人员排名
     * @param form
     * @return
     */
    IPage<BusinessPeople> getRankingBusinessPeople(BasePageForm form,List<String> legalNames);

}
