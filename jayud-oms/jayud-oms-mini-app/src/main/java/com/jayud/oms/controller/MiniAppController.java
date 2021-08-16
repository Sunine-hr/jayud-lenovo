package com.jayud.oms.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.entity.MapEntity;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.*;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.enums.DriverFeedbackStatusEnum;
import com.jayud.oms.model.enums.DriverOrderStatusEnum;
import com.jayud.oms.model.enums.EmploymentFeeStatusEnum;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.security.util.SecurityUtil;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.jayud.common.utils.DateUtils.DATE_TIME_PATTERN;

/**
 * 微信小程序
 */
@RestController
@RequestMapping("/miniApp")
@Slf4j
@Api(tags = "微信小程序")
public class MiniAppController {

    @Autowired
    TmsClient tmsClient;
    @Autowired
    private IDriverOrderInfoService driverOrderInfoService;
    @Autowired
    private ICostInfoService costInfoService;
    @Autowired
    private ICurrencyInfoService currencyInfoService;
    @Autowired
    private IDriverInfoService driverInfoService;
    @Autowired
    private IOrderPaymentCostService orderPaymentCostService;
    @Autowired
    private IDriverEmploymentFeeService driverEmploymentFeeService;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private IWarehouseInfoService warehouseInfoService;
    @Autowired
    private IRegionCityService regionCityService;
    @Autowired
    private IAppletOrderRecordService appletOrderRecordService;
    @Autowired
    private IAppletOrderAddrService appletOrderAddrService;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private MapPositioningService mapPositioningService;
    @Value("${tencentMap.key}")
    private String tencentMapKey;


    @PostMapping("/getDriverOrderTransport")
    @ApiOperation(value = "查看司机中港订单")
    public CommonResult<List<DriverOrderTransportVO>> getDriverOrderTransport(@Valid @RequestBody QueryDriverOrderTransportForm form) {

        form.setDriverId(Long.valueOf(SecurityUtil.getUserInfo()));
        String status = form.getStatus();
        List<DriverOrderTransportVO> tmps = new ArrayList<>();
        if (DriverOrderStatusEnum.ALL.getCode().equals(form.getStatus())) {
            //查询所有订单
            status = null;
        }
        if (DriverOrderStatusEnum.PENDING.getCode().equals(form.getStatus())) {
            status = DriverOrderStatusEnum.IN_TRANSIT.getCode();
        }
        //根据状态查询司机接单信息，如果没有代表还有没有接单
        List<DriverOrderInfo> driverOrderInfos = this.driverOrderInfoService.getDriverOrderInfoByStatus(form.getDriverId(), status, form.getDriverId());
        if (CollectionUtils.isEmpty(driverOrderInfos)) {
            if (DriverOrderStatusEnum.IN_TRANSIT.getCode().equals(form.getStatus())
                    || DriverOrderStatusEnum.FINISHED.getCode().equals(form.getStatus())) {
                return CommonResult.success(tmps);
            }
        }


        Map<Long, DriverOrderInfo> map = driverOrderInfos.stream().collect(Collectors.toMap(DriverOrderInfo::getOrderId, tmp -> tmp));


        //组装订单
        form.assemblyOrder(map.keySet());

//        Map<String, Object> costStatus = this.orderInfoService.getCostStatus(null, subOrderNos).getData();
        //查询中港订单信息
        ApiResult result = tmsClient.getDriverOrderTransport(form);
        Gson gson = new Gson();
        Type type = new TypeToken<ApiResult<List<DriverOrderTransportVO>>>() {
        }.getType();
        ApiResult<List<DriverOrderTransportVO>> data = gson.fromJson(gson.toJson(result), type);
        tmps = data.getData();
        if (tmps != null) {
            for (DriverOrderTransportVO driverOrderTransportVO : tmps) {
                if (this.orderPaymentCostService.isCostSubmitted(driverOrderTransportVO.getOrderNo())) {
                    driverOrderTransportVO.setIsFeeSubmitted(true);
                } else {
                    driverOrderTransportVO.setIsFeeSubmitted(false);
                }
                //是否已完成反馈状态
                DriverOrderInfo driverOrderInfo = map.get(driverOrderTransportVO.getId());
                if (driverOrderInfo != null) {
                    driverOrderTransportVO.setRecordStatus(driverOrderInfo.getStatus());
                }
                if (driverOrderInfo != null && DriverOrderStatusEnum.FINISHED.getCode().equals(driverOrderInfo.getStatus())) {
                    driverOrderTransportVO.setIsFeedbackFinish(true);
                } else {
                    driverOrderTransportVO.setIsFeedbackFinish(false);
                }
                //获取节点
                List<Map<String, Object>> process = this.getProcess(driverOrderTransportVO.getOrderNo(),
                        OrderStatusEnum.getCode(driverOrderTransportVO.getStatus()), true, new HashMap<>());
                //当完成签收时同步数据
                if (process.size() != 0) {
                    this.driverOrderInfoService.synchronizeTmsStatus(process.get(0), driverOrderTransportVO.getId());
                }
            }
        }
        if (DriverOrderStatusEnum.CANCEL.getCode().equals(form.getStatus())) {
            tmps = new ArrayList<>(this.appletOrderRecordService.getConvertPendingRecord());
        }
        return CommonResult.success(tmps);
    }

    @PostMapping("/confirmOrderReceiving")
    @ApiOperation(value = "司机确认接单")
    public CommonResult confirmOrderReceiving(@Valid @RequestBody AddDriverOrderTransportForm form) {
        Long driverId = Long.valueOf(SecurityUtil.getUserInfo());
        DriverOrderInfo driverOrderInfo = new DriverOrderInfo()
                .setDriverId(driverId)
                .setOrderId(form.getOrderId())
                .setOrderNo(form.getOrderNo())
                .setStatus(DriverOrderStatusEnum.IN_TRANSIT.getCode());
        DriverOrderInfo tmp = driverOrderInfoService.getByOrderId(form.getOrderId());
        if (tmp != null) {
            return CommonResult.error(400, "该订单已确认过接单");
        }
        //查询派车骑师
        Object sendCarObj = this.tmsClient.getOrderSendCarsByOrderNo(form.getOrderNo()).getData();
        cn.hutool.json.JSONObject sendCarsJson = new cn.hutool.json.JSONObject(sendCarObj);
        driverOrderInfo.setJockeyId(sendCarsJson.getLong("jockeyId"));
        if (this.driverOrderInfoService.saveOrUpdateDriverOrder(driverOrderInfo)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }

    }

    @PostMapping("/driverDispatchList")
    @ApiOperation(value = "司机派车单 orderNo=订单编号")
    public CommonResult driverDispatchList(@RequestBody Map<String, Object> map) {
        String orderNo = MapUtil.getStr(map, "orderNo");
        ApiResult apiResult = this.tmsClient.dispatchList(orderNo);
        return CommonResult.success(apiResult.getData());
    }

    @PostMapping("/initEmploymentFeeBox")
    @ApiOperation(value = "录用费用下拉选项")
    public CommonResult<Map<String, List<InitComboxStrVO>>> initEmploymentFeeBox(@RequestBody Map<String, Object> param) {
        Long orderId = MapUtil.getLong(param, "orderId");
        if (orderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<CostInfo> costInfos = this.costInfoService.getCostInfoByStatus(StatusEnum.ENABLE.getCode());
        List<InitComboxStrVO> boxOne = new ArrayList<>();
        for (CostInfo costInfo : costInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(costInfo.getIdCode());
            comboxStrVO.setName(costInfo.getName());
            boxOne.add(comboxStrVO);
        }

//        this.tmsClient.get
        Object data = this.tmsClient.getTmsById(orderId).getData();
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject(data);
        //币种
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        List<CurrencyInfoVO> currencyInfos = currencyInfoService.findCurrencyInfo(jsonObject.getStr("createdTime"));
        for (CurrencyInfoVO currencyInfo : currencyInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(currencyInfo.getCurrencyCode());
            comboxStrVO.setName(currencyInfo.getCurrencyName());
            comboxStrVO.setNote(String.valueOf(currencyInfo.getExchangeRate()));
            initComboxStrVOS.add(comboxStrVO);
        }

        //币种
//        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
//        List<CurrencyInfo> currencyInfos = currencyInfoService.list();
//        for (CurrencyInfo currencyInfo : currencyInfos) {
//            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
//            comboxStrVO.setCode(currencyInfo.getCurrencyCode());
//            comboxStrVO.setName(currencyInfo.getCurrencyName());
//            initComboxStrVOS.add(comboxStrVO);
//        }
        Map<String, List<InitComboxStrVO>> map = new HashMap<>();
        map.put("costInfos", boxOne);
        map.put("currencys", initComboxStrVOS);

        return CommonResult.success(map);
    }

    /**
     * 录用费用
     */
    @PostMapping("/doEmploymentFee")
    @ApiOperation(value = "录用费用操作")
    public CommonResult doEmploymentFee(@Valid @RequestBody AddDriverEmploymentFeeForm form) {
        //查询是否确认接单
        if (this.driverOrderInfoService.getByOrderId(form.getOrderId()) == null) {
            return CommonResult.error(400, "该订单没有确认接单,无法进行录用操作");
        }
        //获取司机供应商
        Long driverId = Long.parseLong(SecurityUtil.getUserInfo());
        //查询是否费用提交
        if (this.driverEmploymentFeeService.isExist(driverId,
                form.getOrderId(), EmploymentFeeStatusEnum.SUBMITTED.getCode())) {
            return CommonResult.error(400, "该订单费用已提交,无法录用费用");
        }

        //根据中港订单编号查询主订单
        ApiResult result = this.tmsClient.getDriverOrderTransportById(form.getOrderId());
        if (!result.isOk()) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(result.getData()));
        String mainOrderNo = json.getString("mainOrderNo");
        String orderNo = json.getString("orderNo");

        Object sendCarsObj = this.tmsClient.getOrderSendCarsByOrderNo(orderNo).getData();
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject(sendCarsObj);

        //根据子订单ID去找派车信息，然后找到车辆供应商信息 待开发 TODO
        DriverInfoLinkVO driverInfoLink = this.driverInfoService.getDriverInfoLink(driverId);
        List<VehicleInfoVO> vehicleInfoVOList = driverInfoLink.getVehicleInfoVOList();
        String supplierCode = "";
        String supplierName = "";
        for (VehicleInfoVO vehicleInfoVO : vehicleInfoVOList) {
            Long vehicleId = jsonObject.getLong("vehicleId");
            if (vehicleId.equals(vehicleInfoVO.getId())) {
                supplierCode = vehicleInfoVO.getSupplierCode();
                supplierName = vehicleInfoVO.getSupplierName();
            }

        }

        DriverEmploymentFee driverEmploymentFee = new DriverEmploymentFee()
                .setCostCode(form.getCostCode())
                .setAmount(form.getAmount())
                .setCurrencyCode(form.getCurrencyCode())
                .setFiles(StringUtils.getFileStr(form.getFileViews()))
                .setFileName(StringUtils.getFileNameStr(form.getFileViews()))
                .setMainOrderNo(mainOrderNo)
                .setOrderId(form.getOrderId())
                .setOrderNo(orderNo)
                .setSupplierCode(supplierCode)
                .setSupplierName(supplierName)
                .setCreateTime(LocalDateTime.now())
                .setStatus(EmploymentFeeStatusEnum.SUBMIT.getCode())
                .setDriverId(driverId);

        //保存费用
        this.driverEmploymentFeeService.save(driverEmploymentFee);

        return CommonResult.success();
    }

    @PostMapping("/feeSubmission")
    @ApiOperation(value = "费用提交 orderId=中港订单Id,orderNo=中港订单编号")
    public CommonResult feeSubmission(@RequestBody Map<String, String> map) {
        //获取司机供应商
        Long driverId = Long.parseLong(SecurityUtil.getUserInfo());
        Long orderId = MapUtil.getLong(map, "orderId");
        String orderNo = MapUtil.getStr(map, "orderNo");
        if (orderId == null ||
                org.apache.commons.lang.StringUtils.isEmpty(orderNo)) {
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }

        //查询是否存在录用费用项
        if (!this.driverEmploymentFeeService.isExist(driverId, orderId,
                EmploymentFeeStatusEnum.SUBMIT.getCode())) {
            return CommonResult.error(400, "不存在录用费用项,不能费用提交");
        }
        //查询所有提交的费用项
        List<DriverEmploymentFee> employmentFee = this.driverEmploymentFeeService.getEmploymentFee(orderNo,
                driverId, EmploymentFeeStatusEnum.SUBMIT.getCode());
        //批量费用提交
        this.driverEmploymentFeeService.feeSubmission(employmentFee);
        return CommonResult.success();
    }


    @PostMapping("/getDriverOrderTransportDetail")
    @ApiOperation(value = "根据订单编号查询司机的中港订单详情 orderNo=订单编号,orderId=订单id")
    public CommonResult<DriverOrderTransportVO> getDriverOrderTransportDetail(@RequestBody Map<String, String> map) {
        QueryDriverOrderTransportForm form = new QueryDriverOrderTransportForm();
        form.setOrderNo(map.get("orderNo"));
        String orderId = map.get("orderId");
        if (org.apache.commons.lang.StringUtils.isEmpty(form.getOrderNo()) ||
                org.apache.commons.lang.StringUtils.isEmpty(orderId)) {
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }

        //查询中港订单信息
        ApiResult result = tmsClient.getDriverOrderTransport(form);
        Gson gson = new Gson();
        Type type = new TypeToken<ApiResult<List<DriverOrderTransportVO>>>() {
        }.getType();
        ApiResult<List<DriverOrderTransportVO>> response = gson.fromJson(gson.toJson(result), type);

        List<DriverOrderTransportVO> datas = response.getData();
        if (CollectionUtils.isEmpty(datas)) {
            log.warn("不存在该订单详情");
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        DriverOrderTransportVO driverOrderTransportVO = datas.get(0);
        DriverOrderInfo tmp = this.driverOrderInfoService.getByOrderId(Long.parseLong(orderId));
        if (tmp == null) {
            driverOrderTransportVO.setAcceptOrder(false);
            driverOrderTransportVO.setIsFeeSubmitted(false);
            return CommonResult.success(driverOrderTransportVO);
        } else {
            driverOrderTransportVO.setAcceptOrder(true);

        }

        //查询订单录用费用明细
        List<DriverOrderPaymentCostVO> orderPaymentCosts = this.orderPaymentCostService.getDriverOrderPaymentCost(form.getOrderNo());
        //判断是否已经提交费用，如果没有，设置待交费用状态，否则已提交
        if (orderPaymentCosts.size() > 0) {
            driverOrderTransportVO.setIsFeeSubmitted(true);
        } else {
            List<DriverEmploymentFeeVO> employmentFee = this.driverEmploymentFeeService.getEmploymentFeeInfo(form.getOrderNo());
            orderPaymentCosts = ConvertUtil.convertList(employmentFee, DriverOrderPaymentCostVO.class);
            driverOrderTransportVO.setIsFeeSubmitted(false);
        }
        for (DriverOrderPaymentCostVO orderPaymentCost : orderPaymentCosts) {
            //组装附件
            orderPaymentCost.assemblyAnnex();
        }
        driverOrderTransportVO.setEmploymentFeeDetails(orderPaymentCosts);
        //计算费用
        driverOrderTransportVO.calculateTotalCost();

        return CommonResult.success(driverOrderTransportVO);
    }

    @ApiOperation(value = "查询司机信息")
    @PostMapping(value = "/getDriverInfo")
    public CommonResult getDriverInfo() {
        String driverId = SecurityUtil.getUserInfo();
        DriverInfoLinkVO driverInfo = this.driverInfoService.getDriverInfoLink(Long.parseLong(driverId));
        return CommonResult.success(driverInfo);
    }

    @ApiOperation(value = "设置新密码")
    @PostMapping(value = "/updatePassword")
    public CommonResult updatePassword(@Valid @RequestBody PasswordForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            return CommonResult.error(400, "密码不一致");
        }
        Long driverId = Long.parseLong(SecurityUtil.getUserInfo());
        DriverInfo driverInfo = this.driverInfoService.getById(driverId);
        if (!driverInfo.getPassword().equals(MD5.encode(form.getOldPassword()))) {
            return CommonResult.error(400, "密码错误");
        }
        this.driverInfoService.updateById(new DriverInfo().setId(driverId)
                .setPassword(MD5.encode(form.getPassword())));
        return CommonResult.success();
    }

    @ApiOperation(value = "查看我的订单数量")
    @PostMapping(value = "/getMyOrderNum")
    public CommonResult getMyOrderNum() {
        Long driverId = Long.parseLong(SecurityUtil.getUserInfo());

        List<DriverOrderInfo> driverOrderInfos = this.driverOrderInfoService
                .getDriverOrderInfoByStatus(driverId, null, driverId);

        //订单数目分组
        Map<String, Integer> map = new HashMap<>();
        //统计待接单
        List<String> orderNos = new ArrayList<>();
        for (DriverOrderInfo driverOrderInfo : driverOrderInfos) {
            map.merge(driverOrderInfo.getStatus(), 1, Integer::sum);
            orderNos.add(driverOrderInfo.getOrderNo());
        }
        //获取待接单数量
        ApiResult result = this.tmsClient.getDriverPendingOrderNum(driverId, orderNos);

        List<AppletOrderRecord> appletOrderRecords = this.appletOrderRecordService.getByCondition(new AppletOrderRecord().setRecordStatus(1));

        //重组数据
        Map<String, Object> response = new HashMap<>();
        Integer transitNum = map.get(DriverOrderStatusEnum.IN_TRANSIT.getCode());
        Integer finishedNum = map.get(DriverOrderStatusEnum.FINISHED.getCode());
        response.put("pending", result.getData() == null ? 0 : result.getData());
        response.put("transitNum", transitNum == null ? 0 : transitNum);
        response.put("finishedNum", finishedNum == null ? 0 : finishedNum);
        response.put("cancelNum", appletOrderRecords.size());

        return CommonResult.success(response);
    }

    @ApiOperation(value = "查询反馈状态,orderNo=订单编号")
    @PostMapping(value = "/getFeedbackStatus")
    public CommonResult getFeedbackStatus(@RequestBody Map<String, String> map) {
        String orderNo = map.get("orderNo");
        if (org.apache.commons.lang.StringUtils.isEmpty(orderNo)) {
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        List<Map<String, Object>> process = this.getProcess(orderNo, null, false, new HashMap<>());
        return CommonResult.success(process);
    }


    @ApiOperation(value = "司机反馈状态")
    @PostMapping(value = "/doDriverFeedbackStatus")
    public CommonResult doDriverFeedbackStatus(@RequestBody DriverFeedbackStatusForm form) {
        //缓存值
        Map<String, Object> cacheValue = new HashMap<>();
        //根据中港订单编号查询主订单
        ApiResult result = this.tmsClient.getDriverOrderTransportById(form.getOrderId());
        if (!result.isOk()) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        if (!this.driverOrderInfoService.isExistOrder(form.getOrderId())) {
            return CommonResult.error(400, "请先接单,才能进行后续操作");
        }

        //获取主订单编号
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(result.getData()));
        String mainOrderNo = json.getString("mainOrderNo");
        String orderNo = json.getString("orderNo");
        //获取主订单id
        Long mainOrderId = this.orderInfoService.getIdByOrderNo(mainOrderNo);
        form.setMainOrderId(mainOrderId);
        //获取当前流程节点状态
        List<Map<String, Object>> process = this.getProcess(orderNo, json.getString("status"), true, cacheValue);
        //判断节点状态是否和用户操作一致
        Map<String, Object> map = process.get(0);
        Integer currentStatus = MapUtil.getInt(map, "id");
        Boolean isEdit = MapUtil.getBool(map, "isEdit");
        if (!currentStatus.equals(form.getOptStatus())) {
            log.warn("操作流程不一致，前台传入状态{},订单现阶段状态{}", form.getOptStatus(), currentStatus);
            return CommonResult.error(ResultEnum.OPR_FAIL);
        } else {
            if (!isEdit) {
                return CommonResult.error(400, "正在审核中，请等待审核结果");
            }
        }

        //根据状态进行业务操作
        switch (form.getOptStatus()) {
            case 0:
                form.setCmd(CommonConstant.CAR_TAKE_GOODS);
                break;
            case 1:
                if (form.getCarWeighNum() == null) {
                    return CommonResult.error(400, "请填写过磅重量");
                }
                form.setCmd(CommonConstant.CAR_WEIGH);
                break;
            case 2:
                if (org.apache.commons.lang.StringUtils.isEmpty(form.getStatus())) {
                    return CommonResult.error(400, "请选择通关状态");
                }
//                if (MapUtil.getInt(cacheValue, "deliveryAddressNum") == 1) {//送到目的地址，要补入仓出仓数据
//                    form.setNextCmd(CommonConstant.CAR_SEND);
//                }
                //虚拟仓是直接送到目的地,实际仓库送到中转仓库
                if (MapUtil.getBool(cacheValue, "isVirtual")) {//送到目的地址，要补入仓出仓数据
                    form.setNextCmd(CommonConstant.CAR_SEND);
                }
                form.setCmd(CommonConstant.CAR_GO_CUSTOMS);
                break;
            case 3:
                form.setCmd(CommonConstant.CAR_SEND);
                break;
            case 4:
//                if (MapUtil.getInt(cacheValue, "deliveryAddressNum") > 1) {
//                    form.setCmd(CommonConstant.CAR_ENTER_WAREHOUSE);
//                } else {
//                    form.setCmd(CommonConstant.CONFIRM_SIGN_IN);
//                }
                if (!MapUtil.getBool(cacheValue, "isVirtual")) {
                    form.setCmd(CommonConstant.CAR_ENTER_WAREHOUSE);
                } else {
                    form.setCmd(CommonConstant.CONFIRM_SIGN_IN);
                }
                //执行反馈状态操作
                if (HttpStatus.SC_OK == this.tmsClient.doDriverFeedbackStatus(form).getCode()) {
                    //修改司机接单状态
                    this.driverOrderInfoService.updateStatus(form.getOrderId(), DriverOrderStatusEnum.FINISHED.getCode());
                    return CommonResult.success();
                } else {
                    return CommonResult.error(ResultEnum.OPR_FAIL);
                }
        }
        this.tmsClient.doDriverFeedbackStatus(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "查询签收地址 orderNo=中港订单编号")
    @PostMapping(value = "/getSignatureAddress")
    public CommonResult getSignatureAddress(@RequestBody Map<String, String> map) {
        String orderNo = map.get("orderNo");
        if (org.apache.commons.lang.StringUtils.isEmpty(orderNo)) {
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        QueryDriverOrderTransportForm form = new QueryDriverOrderTransportForm();
        form.setOrderNo(orderNo);
        ApiResult result = tmsClient.getDriverOrderTransport(form);
        Gson gson = new Gson();
        Type type = new TypeToken<ApiResult<List<DriverOrderTransportVO>>>() {
        }.getType();
        ApiResult<List<DriverOrderTransportVO>> data = gson.fromJson(gson.toJson(result), type);
        List<DriverOrderTransportVO> tmps = data.getData();
        if (CollectionUtils.isEmpty(tmps)) {
            return CommonResult.success();
        }
        DriverOrderTransportVO transportVO = tmps.get(0);

        //查询送货地址
//        ApiResult<List<DriverOrderTakeAdrVO>> result = this.tmsClient.getDriverOrderTakeAdrByOrderNo(Collections.singletonList(orderNo), 2);
//        if (!result.isOk()) {
//            log.warn("查询送货地址失败");
//            return CommonResult.error(ResultEnum.OPR_FAIL);
//        }
//        List<DriverOrderTakeAdrVO> orderTakeAdrVOs = result.getData();
        Map<String, Object> response = new HashMap<>();
        String address = "";
        if (transportVO.getIsVirtual() != null && transportVO.getIsVirtual()) { //只有一个送货地址，取送货地址
            List<DriverOrderTakeAdrVO> receivingGoodsList = transportVO.getReceivingGoodsList();
            address = CollectionUtils.isEmpty(receivingGoodsList) ? receivingGoodsList.get(0).getAddress() : "";
        } else {
            address = transportVO.getAddress();
        }

        response.put("address", address);
        //送货地址大于一个，取中转仓地址
        //查询派车单
//        result = this.tmsClient.getOrderSendCarsByOrderNo(orderNo);
//        if (!result.isOk()) {
//            log.warn("查询派车单信息失败");
//            return CommonResult.error(ResultEnum.OPR_FAIL);
//        }
//        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(result.getData()));
//        Long warehouseInfoId = json.getLong("warehouseInfoId");
//
//        WarehouseInfo warehouseInfo = this.warehouseInfoService.getById(warehouseInfoId);
        //查询中转仓地址名称
//        Collection<RegionCity> regionCities = regionCityService.listByIds(Arrays.asList(warehouseInfo.getStateCode(), warehouseInfo.getCityCode(), warehouseInfo.getAreaCode()));
//        //拼接地址
//        StringBuilder sb = new StringBuilder();
//        regionCities.forEach(tmp -> sb.append(tmp.getName()));
//        response.put("address", sb.append(warehouseInfo.getAddress()));
        return CommonResult.success(response);
    }


    @ApiOperation(value = "登出")
    @PostMapping(value = "/logOut")
    public CommonResult logOut() {
        SecurityUtil.logout(SecurityUtil.getUserInfo());
        return CommonResult.success();
    }

    /**
     * 获取流程
     */
    private List<Map<String, Object>> getProcess(String orderNo, String status, boolean isGetNot, Map<String, Object> cacheValue) {
        //TODO 后面要改就用这个
//        ApiResult resultOne = this.tmsClient.getTmsOrderByOrderNo(orderNo);
//        if (!resultOne.isOk()) {
//            log.error("远程调用查询中港订单失败");
//            throw new JayudBizException(ResultEnum.OPR_FAIL);
//        }
//        if (status == null) {
//            status = new cn.hutool.json.JSONObject(resultOne.getData()).getStr("status");
//        }

        if (status == null) {
            ApiResult resultOne = this.tmsClient.getOrderTransportStatus(orderNo);
            if (!resultOne.isOk()) {
                log.error("远程调用查询中港订单状态失败");
                throw new JayudBizException(ResultEnum.OPR_FAIL);
            }
            status = resultOne.getData().toString();
        }

        //查询送货地址数量，判断是送到中转仓库（1个以上），还是目的地（一个）
        ApiResult resultTwo = this.tmsClient.getDeliveryAddressNum(orderNo);
        if (!resultTwo.isOk()) {
            log.error("远程调用查询送货地址数量失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        //是否虚拟仓
        ApiResult<Boolean> resultThree = this.tmsClient.isVirtualWarehouseByOrderNo(orderNo);
        if (!resultThree.isOk()) {
            log.error("远程调用是否虚拟仓失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        Boolean isVirtual = resultThree.getData();
        cacheValue.put("deliveryAddressNum", resultTwo.getData());
        cacheValue.put("isVirtual", isVirtual);
//        int num = Integer.parseInt(resultTwo.getData().toString());
//        return DriverFeedbackStatusEnum.constructionProcess(status,
//                num > 1 ? DriverFeedbackStatusEnum.THREE : null, isGetNot);
        return DriverFeedbackStatusEnum.constructionProcess(status,
                !isVirtual ? DriverFeedbackStatusEnum.THREE : null, isGetNot);
    }

    @ApiOperation(value = "消息通知")
    @PostMapping(value = "/messageNotice")
    public CommonResult messageNotice() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", DateUtils.format(new Date(), DATE_TIME_PATTERN));
        map.put("headline", "佳裕达物流科技");
        map.put("subhead", "关于国庆节、中秋节放假");
        map.put("message", "国庆节、中秋节放假安排....");
        return CommonResult.success(map);
    }


    @PostMapping("/getLaAndLongitude")
    @ApiOperation(value = "获取经纬度")
    public CommonResult<Map<String, Object>> getLaAndLongitude(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        QueryDriverOrderTransportForm form = new QueryDriverOrderTransportForm();
        form.setOrderIds(Collections.singletonList(id));

        //查询中港订单信息
        Object result = tmsClient.getDriverOrderTransport(form).getData();
        Gson gson = new Gson();
        Type type = new TypeToken<List<DriverOrderTransportVO>>() {
        }.getType();
        List<DriverOrderTransportVO> data = gson.fromJson(gson.toJson(result), type);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> startPoint = new HashMap<>();
        Map<String, Object> endPoint = new HashMap<>();

        if (CollectionUtils.isNotEmpty(data)) {
            DriverOrderTransportVO driverOrderTransportVO = data.get(0);
            Boolean isVirtual = driverOrderTransportVO.getIsVirtual();
            DriverOrderTakeAdrVO pickUpAddr = driverOrderTransportVO.getPickUpGoodsList().get(0);
            String loAndLa = pickUpAddr.getLoAndLa();
            if (!StringUtils.isEmpty(loAndLa)) {
                String[] split = loAndLa.split(",");
                startPoint.put("name", pickUpAddr.getAddress());
                startPoint.put("longitude", split[0]);
                startPoint.put("latitude", split[1]);
            }
            DriverOrderTakeAdrVO receivingAddr = driverOrderTransportVO.getReceivingGoods();
            loAndLa = receivingAddr.getLoAndLa();
            if (isVirtual != null && !isVirtual) {
                MapEntity mapEntity = this.mapPositioningService.getTencentMapLaAndLo(receivingAddr.getAddress(), tencentMapKey);
                endPoint.put("name", receivingAddr.getAddress());
                endPoint.put("longitude", mapEntity.getLongitude());
                endPoint.put("latitude", mapEntity.getLatitude());
            } else {
                if (!StringUtils.isEmpty(loAndLa)) {
                    String[] split = loAndLa.split(",");
                    endPoint.put("name", receivingAddr.getAddress());
                    endPoint.put("longitude", split[0]);
                    endPoint.put("latitude", split[1]);
                }
            }

        }
        resultMap.put("startPoint", startPoint);
        resultMap.put("endPoint", endPoint);
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "取消订单")
    @PostMapping(value = "/cancel")
    public CommonResult cancel(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        QueryDriverOrderTransportForm form = new QueryDriverOrderTransportForm();
        form.setOrderIds(Collections.singletonList(id));
        //查询中港订单信息
        Object result = tmsClient.getDriverOrderTransport(form).getData();
        Gson gson = new Gson();
        Type type = new TypeToken<List<DriverOrderTransportVO>>() {
        }.getType();
        List<DriverOrderTransportVO> data = gson.fromJson(gson.toJson(result), type);
        if (CollectionUtils.isNotEmpty(data)) {
            DriverOrderTransportVO tmp = data.get(0);
            if (!OrderStatusEnum.TMS_T_4.getDesc().equals(tmp.getStatus())) {
                return CommonResult.error(400, "只有待接单状态下才能取消订单");
            }
            AppletOrderRecord appletOrderRecord = ConvertUtil.convert(tmp, AppletOrderRecord.class);
            appletOrderRecord.setStatus(tmp.getStatus());
            appletOrderRecord.setRecordStatus(1);
            appletOrderRecord.setOrderId(tmp.getId());
            List<AppletOrderAddr> pickUpAddrs = ConvertUtil.convertList(tmp.getPickUpGoodsList(), AppletOrderAddr.class);
            if (!CollectionUtils.isEmpty(tmp.getReceivingGoodsList())) {
                List<AppletOrderAddr> receivingUpAddrs = ConvertUtil.convertList(tmp.getReceivingGoodsList(), AppletOrderAddr.class);
                pickUpAddrs.addAll(receivingUpAddrs);
            }
            RejectTmsOrderForm rejectTmsOrderForm = new RejectTmsOrderForm();
            rejectTmsOrderForm.setCmd(OrderStatusEnum.TMS_T_5_1.getCode());
            rejectTmsOrderForm.setOrderId(tmp.getId());
            rejectTmsOrderForm.setRejectOptions(2);
            CommonResult commonResult = this.tmsClient.rejectOrder(rejectTmsOrderForm);
            if (ResultEnum.SUCCESS.getCode().equals(commonResult.getCode())) {
                this.appletOrderRecordService.save(appletOrderRecord);
                pickUpAddrs.forEach(e -> e.setAppletOrderRecordId(appletOrderRecord.getId()));
                this.appletOrderAddrService.saveBatch(pickUpAddrs);
            } else {
                log.warn("取消订单失败 报错信息:" + commonResult.getMsg());
                return CommonResult.error(400, "取消订单失败");
            }
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "获取司机账单费用")
    @PostMapping(value = "/getDriverBillCost")
    public CommonResult getDriverBillCost(@RequestBody Map<String, Object> map) {
        String time = MapUtil.getStr(map, "time");
        Long driverId = Long.valueOf(SecurityUtil.getUserInfo());
        List<DriverOrderInfo> driverOrderInfos = this.driverOrderInfoService.getDriverOrderInfoByStatus(driverId, null, driverId);
        List<String> orderNos = driverOrderInfos.stream().map(DriverOrderInfo::getOrderNo).collect(Collectors.toList());
        List<String> status = new ArrayList<>();
        status.add(OrderStatusEnum.COST_0.getCode());
        status.add(OrderStatusEnum.COST_2.getCode());
        status.add(OrderStatusEnum.COST_3.getCode());

        List<DriverEmploymentFee> employmentFees = this.driverEmploymentFeeService.getByOrderNos(orderNos, EmploymentFeeStatusEnum.SUBMITTED.getCode());
        if (CollectionUtils.isEmpty(employmentFees)) {
            return CommonResult.success();
        }
        List<Long> employIds = employmentFees.stream().map(DriverEmploymentFee::getId).collect(Collectors.toList());

        List<DriverBillCostVO> driverBillCost = this.orderPaymentCostService.getDriverBillCost(orderNos, status, time, employIds);
        List<Map<String, Object>> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(driverBillCost)) {
            Map<String, String> currencyNameMap = this.currencyInfoService.initCurrencyInfo().stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
            Map<String, Map<String, Object>> tmpMap = new HashMap<>();
            for (DriverBillCostVO driverBillCostVO : driverBillCost) {
                Map<String, Object> tmp = tmpMap.get(driverBillCostVO.getOrderNo());
                if (tmp == null) {
                    tmp = new HashMap<>();
                    tmp.put("orderNo", driverBillCostVO.getOrderNo());
                    tmp.put("operationTime", driverBillCostVO.getOperationTime());
                    tmp.put("amount", driverBillCostVO.getAmount() + " " + currencyNameMap.get(driverBillCostVO.getCurrencyCode()));
                } else {
                    tmp.put("amount", tmp.get("amount") + "," + driverBillCostVO.getAmount() + " " + currencyNameMap.get(driverBillCostVO.getCurrencyCode()));
                }
                tmpMap.put(driverBillCostVO.getOrderNo(), tmp);
            }
            tmpMap.forEach((k, v) -> {
                Object amountObj = v.get("amount");
                String amount = Utilities.calculatingCosts(Arrays.asList(amountObj.toString()));
                v.put("amount", amount);
                list.add(v);
            });
        }

        return CommonResult.success(list);
    }


    @ApiOperation(value = "获取司机账单费用详情")
    @PostMapping(value = "/getDriverBillCostInfo")
    public CommonResult<List<Map<String, Object>>> getDriverBillCostInfo(@RequestBody Map<String, Object> map) {
        String orderNo = MapUtil.getStr(map, "orderNo");
        if (StringUtils.isEmpty(orderNo)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Long driverId = Long.valueOf(SecurityUtil.getUserInfo());
        List<String> status = new ArrayList<>();
        status.add(OrderStatusEnum.COST_0.getCode());
        status.add(OrderStatusEnum.COST_2.getCode());
        status.add(OrderStatusEnum.COST_3.getCode());

        List<DriverEmploymentFee> employmentFees = this.driverEmploymentFeeService.getByOrderNos(Arrays.asList(orderNo), EmploymentFeeStatusEnum.SUBMITTED.getCode());
        if (CollectionUtils.isEmpty(employmentFees)) {
            return CommonResult.success();
        }
        List<Long> employIds = employmentFees.stream().map(DriverEmploymentFee::getId).collect(Collectors.toList());


        List<DriverBillCostVO> driverBillCost = this.orderPaymentCostService.getDriverBillCost(Arrays.asList(orderNo), status, null, employIds);
        List<Map<String, Object>> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(driverBillCost)) {
            Map<String, String> currencyNameMap = this.currencyInfoService.initCurrencyInfo().stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
            Map<String, String> costInfoMap = this.costInfoService.findCostInfo().stream().collect(Collectors.toMap(e -> e.getIdCode(), e -> e.getName()));
            Object url = this.fileClient.getBaseUrl().getData();
            for (DriverBillCostVO driverBillCostVO : driverBillCost) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("orderNo", driverBillCostVO.getOrderNo());
                tmp.put("costName", costInfoMap.get(driverBillCostVO.getCostCode()));
                tmp.put("amount", driverBillCostVO.getAmount() + " " + currencyNameMap.get(driverBillCostVO.getCurrencyCode()));
                tmp.put("fileViewList", StringUtils.getFileViews(driverBillCostVO.getFiles(), driverBillCostVO.getFileName(), url.toString()));
                list.add(tmp);
            }
        }

        return CommonResult.success(list);
    }

}
