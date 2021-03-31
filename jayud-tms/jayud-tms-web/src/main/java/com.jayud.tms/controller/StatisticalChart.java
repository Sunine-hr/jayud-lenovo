package com.jayud.tms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.tms.model.bo.BasePageForm;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.vo.OrderTransportVO;
import com.jayud.tms.model.vo.statistical.BusinessPeople;
import com.jayud.tms.model.vo.statistical.TVOrderTransportVO;
import com.jayud.tms.service.StatisticalChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Api(tags = "中港统计图")
@RequestMapping(value = "statisticalChart")
public class StatisticalChart {

    @Autowired
    private StatisticalChartService statisticalChartService;

    @ApiModelProperty(value = "订单数扇形统计图")
    @RequestMapping(value = "/api/getOrderNumSectorChart")
    public ApiResult<Map<String, Object>> getOrderNumSectorChart() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("name", "中港");

        return ApiResult.ok();
    }

    @ApiModelProperty(value = "大屏展示订单业务信息")
    @RequestMapping(value = "/showTV")
    public CommonResult<CommonPageResult<TVOrderTransportVO>> showTVOne(BasePageForm form) {

        List<String> legalNames = new ArrayList<>();
        legalNames.add("深圳市佳裕达国际货运代理有限公司");
        legalNames.add("深圳市佳裕达物流科技有限公司");
        form.setPageSize(1000000000);
        //中港订单信息
        IPage<TVOrderTransportVO> page = statisticalChartService.findTVShowOrderByPage(form,legalNames);
        CommonPageResult<TVOrderTransportVO> pageVO = new CommonPageResult<>(page);
        //统计业务员排名

        return CommonResult.success(pageVO);
    }
    @ApiModelProperty(value = "中港业务人员排名")
    @RequestMapping(value = "/getRankingBusinessPeople")
    public CommonResult<CommonPageResult<BusinessPeople>> getRankingBusinessPeople(BasePageForm form) {
        //业务人员排名
        List<String> legalNames = new ArrayList<>();
        legalNames.add("深圳市佳裕达国际货运代理有限公司");
        legalNames.add("深圳市佳裕达物流科技有限公司");
        form.setPageSize(1000000000);
        IPage<BusinessPeople> page = statisticalChartService.getRankingBusinessPeople(form,legalNames);
        CommonPageResult<BusinessPeople> pageVO = new CommonPageResult<>(page);

        return CommonResult.success(pageVO);
    }



}
