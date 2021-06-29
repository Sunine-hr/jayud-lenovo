package com.jayud.oceanship.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.oceanship.bo.AddSeaOrderForm;
import com.jayud.oceanship.feign.OauthClient;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.po.SeaOrder;
import com.jayud.oceanship.service.ICabinetSizeNumberService;
import com.jayud.oceanship.service.ISeaOrderService;
import com.jayud.oceanship.service.ISeaReplenishmentService;
import com.jayud.oceanship.vo.CabinetSizeNumberVO;
import com.jayud.oceanship.vo.SeaOrderInfoVO;
import com.jayud.oceanship.vo.SeaOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 被外部模块调用的处理接口
 *
 * @author
 * @description
 * @Date:
 */
@RestController
@Api(tags = "海运被外部调用的接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    private ISeaOrderService seaOrderService;

    @Autowired
    private ICabinetSizeNumberService cabinetSizeNumberService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private ISeaReplenishmentService seaReplenishmentService;
    @Autowired
    private OmsClient omsClient;

    /**
     * 创建海运单
     *
     * @param addSeaOrderForm
     * @return
     */
    @RequestMapping(value = "/api/oceanship/createOrder")
    public ApiResult<String> createOrder(@RequestBody AddSeaOrderForm addSeaOrderForm) {

        String orderNo = seaOrderService.createOrder(addSeaOrderForm);
        return ApiResult.ok(orderNo);
    }

    /**
     * 根据主订单号获取海运单信息
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderDetails")
    ApiResult<SeaOrderVO> getSeaOrderDetails(@RequestParam("orderNo") String orderNo) {
        SeaOrder seaOrder = seaOrderService.getByMainOrderNO(orderNo);
        SeaOrderVO seaOrderVO = seaOrderService.getSeaOrderByOrderNO(seaOrder.getId());
        //获取柜型数量

        if (CollectionUtils.isEmpty(seaOrderVO.getCabinetSizeNumbers())) {
            List<CabinetSizeNumberVO> cabinetSizeNumberVOS = new ArrayList<>();
            cabinetSizeNumberVOS.add(new CabinetSizeNumberVO());
            seaOrderVO.setCabinetSizeNumbers(cabinetSizeNumberVOS);
        }


        return ApiResult.ok(seaOrderVO);
    }

    /**
     * 根据主订单号集合获取海运订单信息
     *
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderByMainOrderNos")
    ApiResult getSeaOrderByMainOrderNos(@RequestBody List<String> mainOrderNoList) {
        List<SeaOrder> seaOrders = this.seaOrderService.getSeaOrderByOrderNOs(mainOrderNoList);
        return ApiResult.ok(seaOrders);
    }


    @ApiModelProperty(value = "获取菜单待处理数")
    @RequestMapping(value = "/api/oceanship/getMenuPendingNum")
    public ApiResult getMenuPendingNum(@RequestBody List<Map<String, Object>> menusList) {
        if (CollectionUtil.isEmpty(menusList)) {
            return ApiResult.ok();
        }
        Map<String, String> tmp = new HashMap<>();
        tmp.put("海运接单", "S_0");
        tmp.put("海运订船", "S_1");
        tmp.put("订船驳回", "S_3_2");
        tmp.put("确认入仓", "S_2");
        tmp.put("提交补料", "S_3");
        tmp.put("补料审核", "S_4");
        tmp.put("提单草稿确认", "S_5");
        tmp.put("确认装船", "S_6");
        tmp.put("放单确认", "S_7");
        tmp.put("确认到港", "S_8");
        tmp.put("海外代理", "S_9");
        tmp.put("订单签收", "S_10");
        tmp.put("费用审核", "seaCostAudit");
        List<Map<String, Object>> result = new ArrayList<>();

        ApiResult<List<Long>> legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = legalEntityByLegalName.getData();

        for (Map<String, Object> menus : menusList) {

            Map<String, Object> map = new HashMap<>();
            Object title = menus.get("title");
            String status = tmp.get(title);
            Integer num = 0;
            if (status != null) {
                if (status.equals("S_4") || status.equals("S_5") || status.equals("S_7")) {
                    num = this.seaReplenishmentService.getNumByStatus(status, legalIds);
                } else if ("seaCostAudit".equals(status)) { //费用审核
                    List<SeaOrder> list = this.seaOrderService.getByLegalEntityId(legalIds);
                    if (!CollectionUtils.isEmpty(list)) {
                        List<String> orderNos = list.stream().map(SeaOrder::getOrderNo).collect(Collectors.toList());
                        num = this.omsClient.auditPendingExpenses(SubOrderSignEnum.HY.getSignOne(), legalIds, orderNos).getData();
                    }
                } else {
                    num = this.seaOrderService.getNumByStatus(status, legalIds);
                }
            }
            map.put("menusName", title);
            map.put("num", num);
            result.add(map);
        }
        return ApiResult.ok(result);
    }


    /**
     * 根据主订单号集合获取海运订单详情信息
     *
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderInfoByMainOrderNos")
    ApiResult getSeaOrderInfoByMainOrderNos(@RequestBody List<String> mainOrderNoList) {
        List<SeaOrderInfoVO> seaOrders = this.seaOrderService.getSeaOrderInfoByMainOrderNos(mainOrderNoList);
        return ApiResult.ok(seaOrders);
    }

    /**
     * 根据主订单号集合查询中港详情信息
     */
//    @RequestMapping(value = "/api/getTmsOrderInfoByMainOrderNos")
//    public ApiResult<List<SeaOrderInfoVO>> getSeaOrderInfoByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos) {
//        List<SeaOrderInfoVO> list = this.seaOrderService.getSeaOrderInfoByMainOrderNos(mainOrderNos);
//        List<String> orderNos = new ArrayList<>();
//        List<Long> vehicleIds = new ArrayList<>();
//        for (OrderTransportInfoVO orderTransportInfoVO : list) {
//            orderNos.add(orderTransportInfoVO.getOrderNo());
//            OrderSendCarsInfoVO orderSendCars = orderTransportInfoVO.getOrderSendCars();
//            if (orderSendCars != null) {
//                vehicleIds.add(orderSendCars.getVehicleId());
//            }
//        }
//
//        //查询送货/收货地址信息
//        List<OrderTakeAdrInfoVO> takeAdrsList = this.orderTakeAdrService.getOrderTakeAdrInfos(orderNos, null);
//
//        //组装地址
//        for (OrderTransportInfoVO orderTransportInfoVO : list) {
//            orderTransportInfoVO.assemblyAddr(takeAdrsList);
//            orderTransportInfoVO.assemblyVehicleInfos(vehicleInfos);
//        }
//        return ApiResult.ok(list);
//    }

}
