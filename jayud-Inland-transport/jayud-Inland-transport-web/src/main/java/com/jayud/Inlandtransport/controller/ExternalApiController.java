package com.jayud.Inlandtransport.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.jayud.Inlandtransport.feign.OauthClient;
import com.jayud.Inlandtransport.model.bo.AddOrderInlandTransportForm;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.InputOrderInlandTPVO;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportDetails;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.entity.InitChangeStatusVO;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.common.enums.CreateUserTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(tags = "内陆外部调用")
public class ExternalApiController {

    @Autowired
    private IOrderInlandTransportService orderInlandTransportService;
    @Autowired
    private OauthClient oauthClient;


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
        tmp.assembleOrderInlandSendCarsVO(orderDetails.getOrderInlandSendCarsVO());
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



    @ApiOperation(value = "根据主订单号集合查询内陆订单详情")
    @PostMapping(value = "/api/getInlandOrderInfoByMainOrderNos")
    public ApiResult<List<OrderInlandTransportDetails>> getInlandOrderInfoByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos) {
        List<OrderInlandTransportDetails> list = this.orderInlandTransportService.getInlandOrderInfoByMainOrderNos(mainOrderNos);
        return ApiResult.ok(list);
    }


    @ApiModelProperty(value = "获取菜单待处理数")
    @RequestMapping(value = "/api/getMenuPendingNum")
    public ApiResult getMenuPendingNum(@RequestBody List<Map<String, Object>> menusList) {
        if (CollectionUtil.isEmpty(menusList)) {
            return ApiResult.ok();
        }
        Map<String, String> tmp = new HashMap<>();
        tmp.put("内陆接单", "NL_0");
        tmp.put("内陆派车", "NL_1");
        tmp.put("派车驳回", "NL_3_1");
        tmp.put("派车审核", "NL_2");
        tmp.put("确认派车", "NL_3");
        tmp.put("车辆提货", "NL_4");
        tmp.put("货物签收", "NL_5");
//        tmp.put("费用审核","inlandFeeCheck");
        List<Map<String, Object>> result = new ArrayList<>();

        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        for (Map<String, Object> menus : menusList) {

            Map<String, Object> map = new HashMap<>();
            Object title = menus.get("title");
            String status = tmp.get(title);
            Integer num = 0;
            if (status != null) {
                num = this.orderInlandTransportService.getNumByStatus(status, legalIds);
            }
            map.put("menusName", title);
            map.put("num", num);
            result.add(map);
        }
        return ApiResult.ok(result);
    }
}
