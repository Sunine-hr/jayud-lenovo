package com.jayud.tms.controller;


import com.jayud.common.CommonResult;
import com.jayud.common.utils.BeanUtils;
import com.jayud.tms.model.bo.QueryDeliveryAddressForm;
import com.jayud.tms.model.po.DeliveryAddress;
import com.jayud.tms.model.vo.DeliveryAddressVO;
import com.jayud.tms.model.vo.StatisticsDataNumberVO;
import com.jayud.tms.service.IDeliveryAddressService;
import com.jayud.tms.service.IOrderTransportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


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


    @ApiOperation(value = "二期优化1:中港运输各个菜单列表数据量统计")
    @PostMapping(value = "/statisticsDataNumber")
    public CommonResult<StatisticsDataNumberVO> statisticsDataNumber() {
        StatisticsDataNumberVO dataNumberVO = orderTransportService.statisticsDataNumber();
        return CommonResult.success(dataNumberVO);
    }


    @ApiOperation(value = "查询联系人信息")
    @RequestMapping(value = "/getContactInfoByPhone")
    public CommonResult<List<Map<String, Object>>> getContactInfoByPhone() {
        List<DeliveryAddress> deliveryAddresses = this.deliveryAddressService.getLastContactInfo();
        List<Map<String, Object>> list = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(deliveryAddresses)) {
            //去重
            deliveryAddresses = deliveryAddresses.stream().collect(Collectors
                    .collectingAndThen(Collectors.toCollection(
                            () -> new TreeSet<>(Comparator.comparing(DeliveryAddress::getPhone))), ArrayList::new));
            for (DeliveryAddress deliveryAddress : deliveryAddresses) {
                Map<String, Object> response = new HashMap<>();
                response.put(BeanUtils.convertToFieldName(DeliveryAddress::getContacts), deliveryAddress.getContacts());
                response.put("value", deliveryAddress.getPhone());
                response.put(BeanUtils.convertToFieldName(DeliveryAddress::getAddress), deliveryAddress.getAddress());
                list.add(response);
            }
            return CommonResult.success(list);
        }
        return CommonResult.success(list);
    }

    @ApiOperation(value = "获取所有联系人电话")
    @RequestMapping(value = "/getContactPhone")
    public CommonResult<List<String>> getContactPhone() {
        List<DeliveryAddress> deliveryAddresses = this.deliveryAddressService.list();
        List<String> phones = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deliveryAddresses)) {
            phones = deliveryAddresses.stream().map(DeliveryAddress::getPhone).collect(Collectors.toList());
        }
        return CommonResult.success(phones);
    }

}

