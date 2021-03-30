package com.jayud.tms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.vo.OrderTransportVO;
import com.jayud.tms.model.vo.statistical.TVOrderTransportVO;
import com.jayud.tms.service.StatisticalChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public CommonResult<CommonPageResult<OrderTransportVO>> showTVOne(QueryOrderTmsForm form) {


        //中港订单信息
        IPage<TVOrderTransportVO> page = statisticalChartService.findTVShowOrderByPage(form);

        //流程节点图


        CommonPageResult<OrderTransportVO> pageVO = new CommonPageResult(page);
        //统计业务员排名

        return CommonResult.success(pageVO);
    }





}
