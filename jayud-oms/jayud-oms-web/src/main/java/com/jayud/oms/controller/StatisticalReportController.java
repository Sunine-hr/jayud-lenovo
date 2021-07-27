package com.jayud.oms.controller;

import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.QueryStatisticalReport;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.service.ICostCommonService;
import com.jayud.oms.service.IOrderInfoService;
import com.jayud.oms.service.StatisticalReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@Api("统计报表")
@RequestMapping("/statisticalReport")
public class StatisticalReportController {

    @Autowired
    private StatisticalReportService statisticalReportService;


    @ApiOperation("获取客服待服务处理数量")
    @PostMapping("/getCSPendingNum")
    public CommonResult<List<Integer>> getCSPendingNum() {
        List<Map<String, Object>> result = this.statisticalReportService.getCSPendingNum();
        List<Integer> num = result.stream().map(e -> Integer.valueOf(e.get("num").toString())).collect(Collectors.toList());
        return CommonResult.success(num);
    }

    @ApiOperation("主订单汇总")
    @PostMapping("/getMainOrderSummary")
    public CommonResult<List<Map<String, Integer>>> getMainOrderSummary(@RequestBody QueryStatisticalReport form) {
        form.assemblyTime();
        List<Map<String, Integer>> list = this.statisticalReportService.getMainOrderSummary(form);
        return CommonResult.success(list);
    }


    @ApiOperation("订单排行榜")
    @PostMapping("/getOrderRanking")
    public CommonResult<List<Map<String, Object>>> getOrderRanking(@RequestBody QueryStatisticalReport form) {
        form.assemblyTime();
        List<Map<String, Object>> list = this.statisticalReportService.getOrderRanking(form);
        return CommonResult.success(list);
    }

    @ApiOperation("营业额统计")
    @PostMapping("/getTurnoverStatistics")
    public CommonResult<Map<String, Object>> getTurnoverStatistics(@RequestBody QueryStatisticalReport form) {
        form.assemblyTime();
        form.supplementaryTimeData();
        Map<String, Object> map = this.statisticalReportService.getTurnoverStatistics(form);
        return CommonResult.success(map);
    }

    @ApiOperation("汇款情况")
    @PostMapping("/remittanceStatus")
    public CommonResult<Map<String, Object>> remittanceStatus(@RequestBody QueryStatisticalReport form) {
        form.assemblyTime();
        form.supplementaryTimeData();
        Map<String, Object> map = this.statisticalReportService.getTurnoverStatistics(form);
        return CommonResult.success(map);
    }
}
