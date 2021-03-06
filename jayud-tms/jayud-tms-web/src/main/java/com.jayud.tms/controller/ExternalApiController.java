package com.jayud.tms.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.DataControl;
import com.jayud.common.entity.LogisticsTrackVO;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.enums.UserTypeEnum;
import com.jayud.common.utils.JDKUtils;
import com.jayud.tms.feign.FinanceClient;
import com.jayud.tms.feign.OauthClient;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.model.bo.*;
import com.jayud.tms.model.enums.OrderTakeAdrTypeEnum;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.po.TmsExtensionField;
import com.jayud.tms.model.vo.*;
import com.jayud.tms.service.IOrderSendCarsService;
import com.jayud.tms.service.IOrderTakeAdrService;
import com.jayud.tms.service.IOrderTransportService;
import com.jayud.tms.service.ITmsExtensionFieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@Api(tags = "tms对外接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    IOrderTransportService orderTransportService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    private IOrderSendCarsService orderSendCarsService;
    @Autowired
    private IOrderTakeAdrService orderTakeAdrService;
    @Autowired
    private ITmsExtensionFieldService tmsExtensionFieldService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private OrderInTransportController transportController;
    @Autowired
    private FinanceClient financeClient;

    @ApiOperation(value = "创建中港子订单")
    @RequestMapping(value = "/api/createOrderTransport")
    ApiResult createOrderTransport(@RequestBody InputOrderTransportForm form) {
        Boolean result = orderTransportService.createOrderTransport(form);
        return ApiResult.ok(result);
    }


    @ApiOperation(value = "获取中港子订单详情")
    @RequestMapping(value = "/api/getOrderTransport")
    ApiResult getOrderTransport(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        InputOrderTransportVO inputOrderTransportVO = orderTransportService.getOrderTransport(mainOrderNo);
        return ApiResult.ok(inputOrderTransportVO);
    }

    @ApiOperation(value = "获取中港订单号")
    @RequestMapping(value = "/api/getTransportOrderNo")
    ApiResult getTransportOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.MAIN_ORDER_NO, mainOrderNo);
        OrderTransport orderTransport = orderTransportService.getOne(queryWrapper);
        if (orderTransport != null) {
            initChangeStatusVO.setId(orderTransport.getId());
            initChangeStatusVO.setOrderNo(orderTransport.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.ZGYS);
            initChangeStatusVO.setStatus(orderTransport.getStatus());
            initChangeStatusVO.setNeedInputCost(orderTransport.getNeedInputCost());
            return ApiResult.ok(initChangeStatusVO);
        }
        return ApiResult.error();
    }

    @ApiOperation(value = "更改中港单状态")
    @RequestMapping(value = "/api/changeCustomsStatus")
    ApiResult changeTransportStatus(@RequestBody List<TmsChangeStatusForm> form) {
        for (TmsChangeStatusForm tms : form) {
            OrderTransport orderTransport = new OrderTransport();
            orderTransport.setStatus(tms.getStatus());
            orderTransport.setNeedInputCost(tms.getNeedInputCost());
            orderTransport.setUpdatedUser(tms.getLoginUser());
            orderTransport.setUpdatedTime(LocalDateTime.now());
            QueryWrapper<OrderTransport> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq(SqlConstant.ORDER_NO, tms.getOrderNo());
            orderTransportService.update(orderTransport, updateWrapper);
        }
        return ApiResult.ok();
    }

    @ApiOperation(value = "根据中港订单号查询中港订单信息")
    @RequestMapping(value = "/api/getTmsOrderByOrderNo")
    public ApiResult<OrderTransport> getTmsOrderByOrderNo(@RequestParam("orderNo") String orderNo) {
        List<OrderTransport> orderTransports = this.orderTransportService
                .getOrderTmsByCondition(new OrderTransport().setOrderNo(orderNo));
        return ApiResult.ok(orderTransports.size() > 0 ? orderTransports.get(0) : orderTransports);
    }


    @ApiOperation(value = "查询司机的中港订单信息")
    @RequestMapping(value = "/api/getDriverOrderTransport")
    public ApiResult getDriverOrderTransport(@RequestBody QueryDriverOrderTransportForm form) {
        //状态转换成中港订单
        form.convertOrderTransStatus();
        List<DriverOrderTransportVO> list = this.orderTransportService.getDriverOrderTransport(form);
        return ApiResult.ok(list);
    }

    @ApiOperation(value = "根据主键查询司机的中港订单信息")
    @RequestMapping(value = "/api/getDriverOrderTransportById")
    public ApiResult getDriverOrderTransportById(@RequestParam("orderId") Long orderId) {
        return ApiResult.ok(this.orderTransportService.getById(orderId));
    }

    @ApiOperation(value = "派车单PDF格式,orderNo=子订单号")
    @RequestMapping(value = "/api/dispatchList")
    public ApiResult dispatchList(@RequestParam(value = "orderNo") String orderNo) {
        SendCarPdfVO sendCarPdfVO = orderTransportService.initPdfData(orderNo, CommonConstant.ZGYS);
        return ApiResult.ok(sendCarPdfVO);
    }


    @ApiOperation(value = "获取司机待接单数量（小程序）")
    @RequestMapping(value = "/api/getDriverPendingOrderNum")
    public ApiResult getDriverPendingOrderNum(@RequestParam("driverId") Long driverId
            , @RequestParam("orderNos") List<String> orderNos) {
        return ApiResult.ok(this.orderSendCarsService.getDriverPendingOrderNum(driverId, orderNos));
    }


    @ApiOperation(value = "查询送货地址数量")
    @RequestMapping(value = "/api/getDeliveryAddressNum")
    public ApiResult getDeliveryAddressNum(@RequestParam("orderNo") String orderNo) {
        return ApiResult.ok(this.orderTakeAdrService.getDeliveryAddressNum(orderNo));
    }

    @ApiOperation(value = "获取中港订单状态")
    @RequestMapping(value = "/api/getOrderTransportStatus")
    public ApiResult getOrderTransportStatus(@RequestParam("orderNo") String orderNo) {
        return ApiResult.ok(this.orderTransportService.getOrderTransportStatus(orderNo));
    }


    @ApiOperation(value = "司机反馈状态")
    @RequestMapping(value = "/api/doDriverFeedbackStatus")
    public ApiResult doDriverFeedbackStatus(@RequestBody OprStatusForm form) {
        //货物派送操作，要填补入仓和出仓数据
        if (CommonConstant.CAR_SEND.equals(form.getNextCmd())) {
            this.orderTransportService.driverCustomsClearanceVehicles(form);
        } else {
            this.orderTransportService.doDriverFeedbackStatus(form);
        }

        return ApiResult.ok();
    }


    @ApiOperation(value = "查询提货/收货地址")
    @RequestMapping(value = "/api/getDriverOrderTakeAdrByOrderNo")
    public ApiResult<List<DriverOrderTakeAdrVO>> getDriverOrderTakeAdrByOrderNo(@RequestParam("orderNo") List<String> orderNo
            , @RequestParam("oprType") Integer oprType) {
        List<DriverOrderTakeAdrVO> orderTakeAdrs = this.orderTakeAdrService.getDriverOrderTakeAdr(orderNo, oprType);
        return ApiResult.ok(orderTakeAdrs);
    }

    @ApiOperation(value = "查询派车信息")
    @RequestMapping(value = "/api/getOrderSendCarsByOrderNo")
    public ApiResult<OrderSendCars> getOrderSendCarsByOrderNo(@RequestParam("orderNo") String orderNo) {
        OrderSendCars orderSendCars = this.orderSendCarsService.getOrderSendCarsByOrderNo(orderNo);
        return ApiResult.ok(orderSendCars);
    }

    @ApiOperation(value = "删除派车信息")
    @RequestMapping(value = "/api/deleteDispatchInfoByOrderNo")
    public ApiResult deleteDispatchInfoByOrderNo(@RequestParam("orderNo") String orderNo) {
        this.orderSendCarsService.deleteDispatchInfoByOrderNo(orderNo);
        return ApiResult.ok();
    }

    @ApiModelProperty(value = "保存扩展字段")
    @RequestMapping(value = "/api/saveOrUpdateTmsExtensionField")
    public ApiResult<TmsExtensionField> saveOrUpdateTmsExtensionField(@RequestBody String json) {
        TmsExtensionField tmsExtensionField = JSONUtil.toBean(json, TmsExtensionField.class);
        this.tmsExtensionFieldService.saveOrUpdate(tmsExtensionField);
        return ApiResult.ok();
    }

    @ApiModelProperty(value = "根据第三方订单获取中港订单信息")
    @RequestMapping(value = "/api/getTmsOrderByThirdPartyOrderNo")
    public ApiResult getTmsOrderByThirdPartyOrderNo(@RequestParam("thirdPartyOrderNo") String thirdPartyOrderNo) {
        List<OrderTransport> orderTransports = this.orderTransportService.getOrderTmsByCondition(new OrderTransport().setThirdPartyOrderNo(thirdPartyOrderNo));
        return ApiResult.ok(orderTransports.size() > 0 ? orderTransports.get(0) : null);
    }

    @ApiModelProperty(value = "根据主订单号集合查询中港信息")
    @RequestMapping(value = "/api/getTmsOrderByMainOrderNos")
    public ApiResult getTmsOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos) {
//        List<OrderTransport> orderTransports = this.orderTransportService.getTmsOrderByMainOrderNos(mainOrderNos);
//        查询送货/收货地址信息
//        List<String> orderNos = orderTransports.stream().map(OrderTransport::getOrderNo).collect(Collectors.toList());

        return ApiResult.ok(this.orderTransportService.getOrderTransportByMainOrderNo(mainOrderNos));
    }

//    @ApiModelProperty(value = "根据订单号获取供应商信息")
//    @RequestMapping(value = "/api/getSupplierNoBySubOrderNo")
//    public ApiResult getSupplierNoBySubOrderNo(@RequestParam("subOrderNo") String subOrderNo) {
//        //根据订单号获取派车信息
//        OrderSendCars orderSendCars = this.orderSendCarsService.getOrderSendCarsByOrderNo(subOrderNo);
//        if (orderSendCars == null) {
//            return ApiResult.ok();
//        }
//        //根据车辆id查询供应商id
//        orderSendCars.getVehicleId();
//    }

    @ApiModelProperty(value = "获取菜单待处理数")
    @RequestMapping(value = "/api/getMenuPendingNum")
    public ApiResult getMenuPendingNum(@RequestBody List<Map<String, Object>> menusList) {
        if (CollectionUtil.isEmpty(menusList)) {
            return ApiResult.ok();
        }
        Map<String, String> tmp = new HashMap<>();
        tmp.put("运输接单", "T_0");
        tmp.put("运输派车", "T_1");
        tmp.put("驳回重新调用", "T_3_1");
        tmp.put("运输审核", "T_2");
        tmp.put("确认派车", "T_3");
        tmp.put("车辆提货", "T_4");
        tmp.put("车辆过磅", "T_5");
        tmp.put("通关前审核", "T_6");
        tmp.put("通关前复核", "T_7");
        tmp.put("车辆通关", "T_8");
        tmp.put("香港清关", "HK_C_1");
        tmp.put("车辆入仓", "T_9");
        tmp.put("车辆出仓", "T_10");
        tmp.put("车辆派送", "T_13");
        tmp.put("确认签收", "T_14");
        tmp.put("费用审核", "CostAudit");
        tmp.put("应收对账单审核", "zgysReceiverCheck");
        tmp.put("应付对账单审核", "zgysPayCheck");
        List<Map<String, Object>> result = new ArrayList<>();

//        ApiResult<List<Long>> legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
//        List<Long> legalIds = legalEntityByLegalName.getData();
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), UserTypeEnum.EMPLOYEE_TYPE.getCode()).getData();
        Map<String, Integer> reBillNumMap = this.financeClient.getPendingBillStatusNum(null, dataControl.getCompanyIds(), 0, false, SubOrderSignEnum.ZGYS.getSignOne()).getData();
        Map<String, Integer> payBillNumMap = this.financeClient.getPendingBillStatusNum(null, dataControl.getCompanyIds(), 1, false, SubOrderSignEnum.ZGYS.getSignOne()).getData();
        Map<String, Object> datas = new HashMap<>();
        datas.put("zgysReceiverCheck", reBillNumMap);
        datas.put("zgysPayCheck", payBillNumMap);

        log.warn("中港菜单:" + JSONUtil.toJsonStr(dataControl));
        for (Map<String, Object> menus : menusList) {

            Map<String, Object> map = new HashMap<>();
            Object title = menus.get("title");
            String status = tmp.get(title);
            Integer num = 0;
            if (status != null) {
                num = this.orderTransportService.getNumByStatus(status, dataControl, datas);
            }
            map.put("menusName", title);
            map.put("num", num);
            result.add(map);
        }
        return ApiResult.ok(result);
    }

    @ApiModelProperty(value = "获取状态数量")
    @RequestMapping(value = "/api/getNumByStatus")
    public ApiResult getNumByStatus(@RequestParam("cmd") String cmd, @RequestBody DataControl dataControl) {
        if (dataControl == null) {
            return ApiResult.error("权限数据不能为空");
        }
        Map<String, String> tmp = new HashMap<>();
        if ("supplier".equals(cmd)) { //供应商
            tmp.put("派车", "T_1");
            tmp.put("提货", "T_4");
            tmp.put("过磅", "T_5");
            tmp.put("驳回", "T_3_1");
            tmp.put("通关", "T_8");
            tmp.put("派送", "T_13");
            tmp.put("签收", "T_14");
        }

        Map<String, Integer> map = new HashMap<>();
        tmp.forEach((k, v) -> {
            Integer num = this.orderTransportService.getNumByStatus(v, dataControl, new HashMap<>());
            map.put(k, num);
        });
        return ApiResult.ok(map);
    }

    @ApiModelProperty(value = "根据订单号获取送货地址信息(下拉选择)")
    @RequestMapping(value = "/api/initTakeAdrBySubOrderNo")
    public ApiResult initTakeAdrBySubOrderNo(@RequestParam("subOrderNo") String subOrderNo) {
        List<DriverOrderTakeAdrVO> orderTakeAdrs = this.orderTakeAdrService.getDriverOrderTakeAdr(Arrays.asList(subOrderNo),
                OrderTakeAdrTypeEnum.TWO.getCode());
        Set<InitComboxVO> initComboxStrVOS = new HashSet<>();
        for (DriverOrderTakeAdrVO orderTakeAdr : orderTakeAdrs) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setName(orderTakeAdr.getAddress());
            initComboxVO.setId(orderTakeAdr.getId());
            initComboxStrVOS.add(initComboxVO);
        }
        return ApiResult.ok(initComboxStrVOS);
    }


    /**
     * 根据主订单号集合查询中港详情信息
     */
    @RequestMapping(value = "/api/getTmsOrderInfoByMainOrderNos")
    public ApiResult<OrderTransportInfoVO> getTmsOrderInfoByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos) {
        List<OrderTransportInfoVO> list = this.orderTransportService.getTmsOrderInfoByMainOrderNos(mainOrderNos);
        List<String> orderNos = new ArrayList<>();
        List<Long> vehicleIds = new ArrayList<>();
        for (OrderTransportInfoVO orderTransportInfoVO : list) {
            orderNos.add(orderTransportInfoVO.getOrderNo());
            OrderSendCarsInfoVO orderSendCars = orderTransportInfoVO.getOrderSendCars();
            if (orderSendCars != null) {
                vehicleIds.add(orderSendCars.getVehicleId());
            }
        }

        //查询送货/收货地址信息
        List<OrderTakeAdrInfoVO> takeAdrsList = this.orderTakeAdrService.getOrderTakeAdrInfos(orderNos, null);
        //查询车辆信息
        Object vehicleInfos = omsClient.getVehicleInfoByIds(vehicleIds).getData();

        //组装地址
        for (OrderTransportInfoVO orderTransportInfoVO : list) {
            orderTransportInfoVO.assemblyAddr(takeAdrsList);
            orderTransportInfoVO.assemblyVehicleInfos(vehicleInfos);
        }
        return ApiResult.ok(list);
    }

    /**
     * 通关前审核前置条件
     */
    @RequestMapping(value = "/api/preconditionsGoCustomsAudit")
    public ApiResult<List<OrderTransport>> preconditionsGoCustomsAudit() {
        List<OrderTransport> list = this.orderTransportService.preconditionsGoCustomsAudit();
        return ApiResult.ok(list);
    }

    /**
     * 根据单个主订单获取子订单详情
     */
    @RequestMapping(value = "/api/getInfoByMainOrderNo")
    public ApiResult<OrderTransportInfoVO> getInfoByMainOrderNo(@RequestParam("mainOrderNo") String mainOrderNo) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        condition.lambda().select(OrderTransport::getId).eq(OrderTransport::getMainOrderNo, mainOrderNo);
        OrderTransport tmsOrder = this.orderTransportService.getOne(condition);
        OrderTransportInfoVO details = this.orderTransportService.getDetailsById(tmsOrder.getId());
        return ApiResult.ok(details);
    }

    /**
     * 根据子订单号获取中港单详情
     */
    @RequestMapping(value = "/api/getTmsInfoBySubOrderNo")
    public ApiResult<OrderTransportInfoVO> getTmsInfoBySubOrderNo(@RequestParam("orderNo") String orderNo) {
        QueryWrapper<OrderTransport> condition = new QueryWrapper<>();
        condition.lambda().select(OrderTransport::getId).eq(OrderTransport::getOrderNo, orderNo);
        OrderTransport tmsOrder = this.orderTransportService.getOne(condition);
        OrderTransportInfoVO details = this.orderTransportService.getDetailsById(tmsOrder.getId());
        return ApiResult.ok(details);
    }


    /**
     * 根据主订单查询是否虚拟仓
     */
    @RequestMapping(value = "/api/isVirtualWarehouseByOrderNo")
    public ApiResult<Boolean> isVirtualWarehouseByOrderNo(@RequestParam("orderNo") String orderNo) {
        Boolean isVirtual = this.orderTransportService.isVirtualWarehouseByOrderNo(orderNo);
        return ApiResult.ok(isVirtual);
    }

    /**
     * 驳回操作
     */
    @RequestMapping(value = "/api/rejectOrder")
    public CommonResult rejectOrder(@RequestBody RejectOrderForm form) {
        return transportController.rejectOrder(form);
    }

    /**
     * 根据id获取中港信息
     */
    @RequestMapping(value = "/api/getTmsById")
    public ApiResult<OrderTransport> getTmsById(@RequestParam("id") Long id) {
        return ApiResult.ok(this.orderTransportService.getById(id));
    }


    /**
     * 获取司机待接单
     */
    @RequestMapping(value = "/api/getDriverPendingOrder")
    public ApiResult<List<OrderSendCars>> getDriverPendingOrder(@RequestParam("orderNos") List<String> orderNos) {
        QueryWrapper<OrderSendCars> condition = new QueryWrapper<>();
        if (CollectionUtil.isNotEmpty(orderNos)) {
            condition.lambda().notIn(OrderSendCars::getOrderNo, orderNos);
        }
        return ApiResult.ok(this.orderSendCarsService.getBaseMapper().selectList(condition));
    }


    /**
     * 批量更新中港车实时定位GPS
     *
     * @return
     */
    @RequestMapping(value = "/api/batchSyncTMSGPSPositioning")
    public ApiResult batchSyncTMSGPSPositioning() {
//        TMS_T_0("T_0", "待接单"), //仅中港子订单状态用
//                TMS_T_1("T_1", "运输接单"),
//                TMS_T_1_1("T_1_1", "运输接单驳回"),
//                TMS_T_2("T_2", "运输派车"),
//                TMS_T_2_1("T_2_1", "运输派车驳回"),
//                TMS_T_3("T_3", "运输审核"),
//                TMS_T_3_1("T_3_1", "运输审核不通过"),//仅中港子订单状态用,这个是审核派车信息
//                TMS_T_3_2("T_3_2", "运输审核驳回"),

//        List<String> excludeStatus = Arrays.asList(OrderStatusEnum.TMS_T_0.getCode(),
//                OrderStatusEnum.TMS_T_1.getCode(),
//                OrderStatusEnum.TMS_T_1_1.getCode(),
//                OrderStatusEnum.TMS_T_2.getCode(),
//                OrderStatusEnum.TMS_T_2_1.getCode(),
//                OrderStatusEnum.TMS_T_3.getCode(),
//                OrderStatusEnum.TMS_T_3_1.getCode(),
//                OrderStatusEnum.TMS_T_3_2.getCode(),
//                OrderStatusEnum.TMS_T_15.getCode());

        List<String> status = Arrays.asList(
                OrderStatusEnum.TMS_T_4.getCode(),
                OrderStatusEnum.TMS_T_5.getCode(),
                OrderStatusEnum.TMS_T_5_1.getCode(),
                OrderStatusEnum.TMS_T_6.getCode(),
                OrderStatusEnum.TMS_T_7.getCode(),
                OrderStatusEnum.TMS_T_7_1.getCode(),
                OrderStatusEnum.TMS_T_8.getCode(),
                OrderStatusEnum.TMS_T_8_1.getCode(),
                OrderStatusEnum.TMS_T_9.getCode(),
                OrderStatusEnum.TMS_T_9_1.getCode(),
                OrderStatusEnum.TMS_T_9_2.getCode(),
                OrderStatusEnum.TMS_T_10.getCode(),
                OrderStatusEnum.TMS_T_13.getCode(),
                OrderStatusEnum.TMS_T_14.getCode());


        //查询所有确认派车和签收前之间状态
//        List<OrderSendCars> orderSendCars = this.orderSendCarsService.getByExcludeStatus(excludeStatus);
        List<OrderSendCarsVO> orderSendCars = this.orderSendCarsService.getByStatus(status);
        Map<Long, Set<String>> map = orderSendCars.stream().filter(e -> e.getVehicleId() != null).collect(Collectors.groupingBy(OrderSendCarsVO::getVehicleId, Collectors.mapping(OrderSendCarsVO::getOrderNo, Collectors.toSet())));
        Object vehicleInfoObjs = this.omsClient.getVehicleInfoByIds(new ArrayList<>(map.keySet())).getData();
        Map<Long, String> vehicleInfoMap = new JSONArray(vehicleInfoObjs).stream().collect(Collectors.toMap(e -> ((JSONObject) e).getLong("id"), e -> ((JSONObject) e).getStr("plateNumber")));
        Map<String, List<String>> req = new HashMap<>();
        map.forEach((k, v) -> {
            req.put(vehicleInfoMap.get(k), new ArrayList<>(v));
        });
        return this.omsClient.batchSyncGPSPositioning(req);
    }


    /**
     * 批量更新中港车实时定位GPS
     *
     * @return
     */
    @RequestMapping(value = "/api/batchSyncGPSTMSHistoryPositioning")
    public ApiResult batchSyncGPSTMSHistoryPositioning() {
        //查询所有确认派车和签收前之间状态
        List<OrderSendCarsVO> orderSendCars = this.orderSendCarsService.getByStatus(Arrays.asList(OrderStatusEnum.TMS_T_15.getCode()));
        if (CollectionUtil.isEmpty(orderSendCars)) return ApiResult.ok();
        //确认派车时间,完成时间
        List<Long> orderIds = new ArrayList<>();
        for (OrderSendCarsVO orderSendCar : orderSendCars) {
            orderIds.add(orderSendCar.getTransportId());
        }
        List<LogisticsTrackVO> logisticsTrackVOS = omsClient.getLogisticsTrackByOrderIds(orderIds, Arrays.asList(OrderStatusEnum.TMS_T_4.getCode(), OrderStatusEnum.TMS_T_15.getCode()),
                BusinessTypeEnum.ZGYS.getCode()).getData();

        Map<Object, Object> logisticsMap = JDKUtils.getGroupByLastData(logisticsTrackVOS, LogisticsTrackVO::getOrderId, e -> e.getOrderId() + "~" + e.getStatus())
                .stream().filter(e -> e.getOperatorTime() != null).collect(Collectors.toMap(e -> e.getOrderId() + "~" + e.getStatus(), LogisticsTrackVO::getOperatorTime));

        Map<Long, List<OrderSendCarsVO>> map = orderSendCars.stream().filter(e -> e.getVehicleId() != null).collect(Collectors.groupingBy(OrderSendCarsVO::getVehicleId));
        Object vehicleInfoObjs = this.omsClient.getVehicleInfoByIds(new ArrayList<>(map.keySet())).getData();
        Map<Long, String> vehicleInfoMap = new JSONArray(vehicleInfoObjs).stream().collect(Collectors.toMap(e -> ((JSONObject) e).getLong("id"), e -> ((JSONObject) e).getStr("plateNumber")));
        Map<String, List<Map<String, Object>>> req = new HashMap<>();
        map.forEach((k, v) -> {
            List<Map<String, Object>> maps = new ArrayList<>();
            v.forEach(e -> {
                Map<String, Object> orderInfo = new HashMap<>();
                orderInfo.put("orderNo", e.getOrderNo());
                orderInfo.put("startTime", logisticsMap.get(e.getTransportId() + "~" + OrderStatusEnum.TMS_T_4.getCode()));
                orderInfo.put("endTime", logisticsMap.get(e.getTransportId() + "~" + OrderStatusEnum.TMS_T_15.getCode()));
                maps.add(orderInfo);
            });
            req.put(vehicleInfoMap.get(k), maps);
        });
        return this.omsClient.batchSyncGPSHistoryPositioning(req);
    }

    /**
     * 获取中港订单
     */
    @RequestMapping(value = "/api/getOrderTransportList")
    public ApiResult getOrderTransportList(@RequestParam("pickUpTimeStart") String pickUpTimeStart, @RequestParam("pickUpTimeEnd") String pickUpTimeEnd, @RequestParam(value = "orderNo", required = false) String orderNo) {
        List<OrderTransportVO> list = orderTransportService.getOrderTransportList(pickUpTimeStart, pickUpTimeEnd, orderNo);
        return ApiResult.ok(list);
    }



    /**
     * 根据订单mainOrderNo获取中港订单 第三方单号
     */
    @RequestMapping(value = "/api/getOrderTransportOneThirdParty")
    public ApiResult getOrderTransportOneThirdParty(@RequestParam(value = "mainOrderNo") String mainOrderNo ) {
        OrderTransportVO orderTransportOne = orderTransportService.getOrderTransportOne(mainOrderNo);
        return ApiResult.ok(orderTransportOne);
    }

}









    



