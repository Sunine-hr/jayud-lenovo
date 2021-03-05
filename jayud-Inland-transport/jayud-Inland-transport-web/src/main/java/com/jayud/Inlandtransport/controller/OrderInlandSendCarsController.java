package com.jayud.Inlandtransport.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.jayud.Inlandtransport.model.po.OrderInlandSendCars;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.SendCarPdfVO;
import com.jayud.Inlandtransport.service.IOrderInlandSendCarsService;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 内陆派车信息 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-03-04
 */
@RestController
@RequestMapping("/orderInlandSendCars")
public class OrderInlandSendCarsController {
    @Autowired
    private IOrderInlandTransportService orderInlandTransportService;
    @Autowired
    private IOrderInlandSendCarsService orderInlandSendCarsService;

    @ApiOperation(value = "渲染数据,确认派车 orderNo=订单号(主/子),entranceType=入口类型(1主订单,2子订单)")
    @PostMapping(value = "/initPdfData")
    public CommonResult<SendCarPdfVO> initPdfData(@RequestBody Map<String, Object> param) {
        String orderNo = MapUtil.getStr(param, CommonConstant.ORDER_NO);
        Integer entranceType = MapUtil.getInt(param, "entranceType");
        if (StringUtil.isNullOrEmpty(orderNo) && entranceType == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OrderInlandTransport subOrder = null;
        if (entranceType == 1) {
            List<OrderInlandTransport> subOrders = this.orderInlandTransportService.getByCondition(
                    new OrderInlandTransport().setMainOrderNo(orderNo));
            if (CollectionUtil.isEmpty(subOrders)) {
                return CommonResult.error(400, "不存在该订单信息");
            } else {
                subOrder = subOrders.get(0);
            }
        } else {
            subOrder = this.orderInlandTransportService.getByCondition(new OrderInlandTransport().setOrderNo(orderNo)).get(0);
        }
        SendCarPdfVO sendCarPdfVO = orderInlandSendCarsService.initPdfData(subOrder, CommonConstant.NLYS_DESC);
        return CommonResult.success(sendCarPdfVO);
    }


    @ApiOperation(value = "查询派车信息 subOrderId=子订单id")
    @PostMapping(value = "/getInlandSendCars")
    public CommonResult<OrderInlandSendCars> getInlandSendCars(@RequestBody Map<String, Object> param) {
        Long subOrderId = MapUtil.getLong(param, "subOrderId");
        if (subOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<OrderInlandSendCars> list = this.orderInlandSendCarsService.getByCondition(new OrderInlandSendCars().setOrderId(subOrderId));
        return CommonResult.success(list.get(0));
    }
}

