package com.jayud.Inlandtransport.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.Inlandtransport.model.bo.AddOrderInlandTransportForm;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.InputOrderInlandTPVO;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportDetails;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.entity.InitChangeStatusVO;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.common.enums.CreateUserTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(tags = "内陆外部调用")
public class ExternalApiController {

    @Autowired
    private IOrderInlandTransportService orderInlandTransportService;


    @RequestMapping(value = "/api/createOrder")
    public ApiResult<String> createOrder(@RequestBody AddOrderInlandTransportForm form) {
        String orderNo = orderInlandTransportService.createOrder(form);
        return ApiResult.ok(orderNo);
    }

    @ApiOperation(value = "查询内陆订单详情")
    @PostMapping(value = "/api/getOrderDetails")
    public ApiResult<InputOrderInlandTPVO> getOrderDetails(@RequestParam("mainOrderNo") String mainOrderNo) {
        List<OrderInlandTransport> list = this.orderInlandTransportService.getByCondition(new OrderInlandTransport().setMainOrderNo(mainOrderNo));
        OrderInlandTransportDetails orderDetails = this.orderInlandTransportService.getOrderDetails(list.get(0).getId());
        InputOrderInlandTPVO tmp = ConvertUtil.convert(orderDetails, InputOrderInlandTPVO.class);
        return ApiResult.ok(tmp);
    }


    @ApiOperation(value = "获取内陆订单号")
    @RequestMapping(value = "/api/getOrderNo")
    public ApiResult getAirOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
        List<OrderInlandTransport> list = this.orderInlandTransportService.getByCondition(new OrderInlandTransport().setMainOrderNo(mainOrderNo));
        if (CollectionUtils.isNotEmpty(list)) {
            OrderInlandTransport tmp = list.get(0);
            initChangeStatusVO.setId(tmp.getId());
            initChangeStatusVO.setOrderNo(tmp.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.NLYS);
            initChangeStatusVO.setOrderTypeDesc(CommonConstant.NLYS_DESC);
            initChangeStatusVO.setStatus(tmp.getProcessStatus() + "");
            initChangeStatusVO.setNeedInputCost(tmp.getNeedInputCost());
            return ApiResult.ok(initChangeStatusVO);
        }
        return ApiResult.error();
    }

    @ApiOperation(value = "关闭订单")
    @RequestMapping(value = "/api/closeOrder")
    public ApiResult closeOrder(@RequestBody List<SubOrderCloseOpt> form) {
        List<String> orderNos = form.stream().map(SubOrderCloseOpt::getOrderNo).collect(Collectors.toList());
        List<OrderInlandTransport> list = this.orderInlandTransportService.getOrdersByOrderNos(orderNos);
        Map<String, OrderInlandTransport> map = list.stream().collect(Collectors.toMap(OrderInlandTransport::getOrderNo, e -> e));

        for (SubOrderCloseOpt subOrderCloseOpt : form) {
            OrderInlandTransport tmp = map.get(subOrderCloseOpt.getOrderNo());
            OrderInlandTransport inlandTransport = new OrderInlandTransport();
            inlandTransport.setId(tmp.getId());
            inlandTransport.setProcessStatus(ProcessStatusEnum.CLOSE.getCode());
            inlandTransport.setNeedInputCost(subOrderCloseOpt.getNeedInputCost());
            inlandTransport.setUpdateUser(subOrderCloseOpt.getLoginUser());
            inlandTransport.setUpdateTime(LocalDateTime.now());

            this.orderInlandTransportService.updateById(inlandTransport);

        }
        return ApiResult.ok();
    }


    @ApiOperation(value = "根据主订单号集合查询内陆订单")
    @PostMapping(value = "/api/getInlandOrderByMainOrderNos")
    public ApiResult<OrderInlandTransport> getInlandOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos) {
        List<OrderInlandTransport> list = this.orderInlandTransportService.getInlandOrderByMainOrderNos(mainOrderNos);
        return ApiResult.ok(list);
    }
}
