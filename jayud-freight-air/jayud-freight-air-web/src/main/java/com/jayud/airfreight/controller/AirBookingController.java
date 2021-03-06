package com.jayud.airfreight.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.model.vo.AirBookingVO;
import com.jayud.airfreight.service.IAirBookingService;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.JDKUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayud.common.enums.OrderStatusEnum.*;

/**
 * <p>
 * 空运订舱表 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2020-12-03
 */
@RestController
@RequestMapping("/airBooking")
@Api(tags = "空运订舱")
public class AirBookingController {
    @Autowired
    private IAirBookingService airBookingService;

    @ApiOperation(value = "查询订舱信息 airOrderId=空运订单id")
    @PostMapping("/info")
    public CommonResult<AirBookingVO> info(@RequestBody Map<String, String> map) {
        Long airOrderId = MapUtil.getLong(map, "airOrderId");
        if (airOrderId == null) {
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        AirBooking airBooking = this.airBookingService.getEnableByAirOrderId(airOrderId);
        AirBookingVO airBookingVO = ConvertUtil.convert(airBooking, AirBookingVO.class);
        //订舱附件
        //提单附件不用传
        return CommonResult.success(airBookingVO);
    }

    @ApiOperation(value = "获取历史交仓仓库信息")
    @PostMapping(value = "/getHistoryDeliveryAddr")
    public CommonResult<List<Map<String, Object>>> getHistoryDeliveryAddr() {
        List<AirBooking> historyDeliveryAddrs = this.airBookingService.getHistoryDeliveryAddr();

        historyDeliveryAddrs = JDKUtils.getGroupByLastData(historyDeliveryAddrs, AirBooking::getId,
                AirBooking::getDeliveryWarehouse);
        List<Map<String, Object>> tmps = new ArrayList<>();
        for (AirBooking historyDeliveryAddr : historyDeliveryAddrs) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("value", historyDeliveryAddr.getDeliveryWarehouse());
            tmp.put("note", historyDeliveryAddr.getDeliveryAddress());
            tmps.add(tmp);
        }
        return CommonResult.success(tmps);
    }
}

