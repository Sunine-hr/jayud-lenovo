package com.jayud.tms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.entity.DataControl;
import com.jayud.tms.feign.OauthClient;
import com.jayud.tms.model.bo.BasePageForm;
import com.jayud.tms.model.vo.statistical.BusinessPeople;
import com.jayud.tms.model.vo.statistical.TVOrderTransportVO;
import com.jayud.tms.service.IOrderTransportService;
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
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IOrderTransportService orderTransportService;

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
        IPage<TVOrderTransportVO> page = statisticalChartService.findTVShowOrderByPage(form, legalNames);
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
        IPage<BusinessPeople> page = statisticalChartService.getRankingBusinessPeople(form, legalNames);
        CommonPageResult<BusinessPeople> pageVO = new CommonPageResult<>(page);

        return CommonResult.success(pageVO);
    }


    @ApiModelProperty(value = "获取订单待处理数")
    @RequestMapping(value = "/getOrderPendingNum")
    public ApiResult getMenuPendingNum() {
        LinkedHashMap<String, String> tmp = new LinkedHashMap<>();
        tmp.put("运输接单", "T_0");
        tmp.put("运输派车", "T_1");
        tmp.put("驳回重新调用", "T_3_1");
        tmp.put("运输审核", "T_2");
        tmp.put("确认派车", "T_3");
        tmp.put("车辆提货", "T_4");
        tmp.put("车辆过磅", "T_5");
        tmp.put("通关前复核", "T_7");
        tmp.put("车辆通关", "T_8");
        tmp.put("香港清关", "HK_C_1");
        tmp.put("车辆入仓", "T_9");
        tmp.put("车辆出仓", "T_10");
        tmp.put("车辆派送", "T_13");
        tmp.put("确认签收", "T_14");
        List<Map<String, Object>> result = new ArrayList<>();

        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken()).getData();

        tmp.forEach((k, v) -> {
            Map<String, Object> map = new HashMap<>();
            Integer num = this.orderTransportService.getNumByStatus(v, dataControl);
            map.put("name", k);
            map.put("num", num);
            result.add(map);
        });

//        for (Map<String, Object> menus : tmp) {
//
//            Map<String, Object> map = new HashMap<>();
//            Object title = menus.get("title");
//            String status = tmp.get(title);
//            Integer num = 0;
//            if (status != null) {
//                num = this.orderTransportService.getNumByStatus(status, legalIds);
//            }
//            map.put("menusName", title);
//            map.put("num", num);
//            result.add(map);
//        }
        return ApiResult.ok(result);
    }

}
