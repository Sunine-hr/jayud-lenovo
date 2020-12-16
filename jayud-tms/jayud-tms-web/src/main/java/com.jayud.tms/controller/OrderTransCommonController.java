package com.jayud.tms.controller;


import com.jayud.common.CommonResult;
import com.jayud.tms.model.bo.QueryDeliveryAddressForm;
import com.jayud.tms.model.vo.DeliveryAddressVO;
import com.jayud.tms.model.vo.StatisticsDataNumberVO;
import com.jayud.tms.service.IDeliveryAddressService;
import com.jayud.tms.service.IOrderTransportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/common")
@Api(tags = "中港运输通用接口")
public class OrderTransCommonController {

    @Autowired
    IDeliveryAddressService deliveryAddressService;

    @Autowired
    IOrderTransportService orderTransportService;

    @ApiOperation(value = "获取提货/送货地址")
    @PostMapping(value = "/findDeliveryAddress")
    public CommonResult<List<DeliveryAddressVO>> findDeliveryAddress(@RequestBody QueryDeliveryAddressForm form) {
        List<DeliveryAddressVO> addressVOS = deliveryAddressService.findDeliveryAddress(form);
        return CommonResult.success(addressVOS);
    }




    @ApiOperation(value = "中港运输各个菜单列表数据量统计")
    @PostMapping(value = "/statisticsDataNumber")
    public CommonResult<StatisticsDataNumberVO> statisticsDataNumber() {
        StatisticsDataNumberVO dataNumberVO = orderTransportService.statisticsDataNumber();
        return CommonResult.success(dataNumberVO);
    }



}

