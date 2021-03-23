package com.jayud.customs.controller;


import com.jayud.common.CommonResult;
import com.jayud.customs.feign.OmsClient;
import com.jayud.customs.model.vo.StatisticsDataNumberVO;
import com.jayud.customs.service.IOrderCustomsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/common")
@Api(tags = "纯报关通用接口")
public class OrderCustomsCommonController {

    @Autowired
    IOrderCustomsService orderCustomsService;
    @Autowired
    private OmsClient omsClient;

    @ApiOperation(value = "二期优化1:报关各个菜单列表数据量统计")
    @PostMapping(value = "/statisticsDataNumber")
    public CommonResult<StatisticsDataNumberVO> statisticsDataNumber() {
        StatisticsDataNumberVO dataNumberVO = orderCustomsService.statisticsDataNumber();
        return CommonResult.success(dataNumberVO);
    }

    @ApiOperation(value = "下拉选择监管方式")
    @PostMapping(value = "/initSupervisionMode")
    public CommonResult initAirlineCompany() {
        Object data = this.omsClient.initDictByDictTypeCode("supervisionMode").getData();
        return CommonResult.success(data);
    }
}

