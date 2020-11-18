package com.jayud.oms.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.MD5;
import com.jayud.common.utils.StringUtils;
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
        List<DriverOrderInfo> driverOrderInfos = this.driverOrderInfoService.getDriverOrderInfoByStatus(form.getDriverId(), status);
        if (CollectionUtils.isEmpty(driverOrderInfos)) {
            if (DriverOrderStatusEnum.IN_TRANSIT.getCode().equals(form.getStatus())
                    || DriverOrderStatusEnum.FINISHED.getCode().equals(form.getStatus())) {
                return CommonResult.success(tmps);
            }
        }

        Map<Long, DriverOrderInfo> map = driverOrderInfos.stream().collect(Collectors.toMap(DriverOrderInfo::getOrderId, tmp -> tmp));

        //组装订单
        form.assemblyOrder(map.keySet());

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
                if (driverOrderInfo != null && DriverOrderStatusEnum.FINISHED.getCode().equals(driverOrderInfo.getStatus())) {
                    driverOrderTransportVO.setIsFeedbackFinish(true);
                } else {
                    driverOrderTransportVO.setIsFeedbackFinish(false);
                }
            }
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
    public CommonResult<Map<String, List<InitComboxStrVO>>> initEmploymentFeeBox() {
        List<CostInfo> costInfos = this.costInfoService.getCostInfoByStatus(StatusEnum.ENABLE.getCode());
        List<InitComboxStrVO> boxOne = new ArrayList<>();
        for (CostInfo costInfo : costInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(costInfo.getIdCode());
            comboxStrVO.setName(costInfo.getName());
            boxOne.add(comboxStrVO);
        }

        //币种
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        List<CurrencyInfoVO> currencyInfos = currencyInfoService.findCurrencyInfo();
        for (CurrencyInfoVO currencyInfo : currencyInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(currencyInfo.getCurrencyCode());
            comboxStrVO.setName(currencyInfo.getCurrencyName());
            comboxStrVO.setNote(currencyInfo.getExchangeRate());
            initComboxStrVOS.add(comboxStrVO);
        }
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
            return CommonResult.error(400, "改订单费用已提交,无法录用费用");
        }

        //根据中港订单编号查询主订单
        ApiResult result = this.tmsClient.getDriverOrderTransportById(form.getOrderId());
        if (!result.isOk()) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(result.getData()));
        String mainOrderNo = json.getString("mainOrderNo");
        String orderNo = json.getString("orderNo");

        DriverInfoLinkVO driverInfoLink = this.driverInfoService.getDriverInfoLink(driverId);

        DriverEmploymentFee driverEmploymentFee = new DriverEmploymentFee()
                .setCostCode(form.getCostCode())
                .setAmount(form.getAmount())
                .setCurrencyCode(form.getCurrencyCode())
                .setFiles(StringUtils.getFileStr(form.getFileViews()))
                .setFileName(StringUtils.getFileNameStr(form.getFileViews()))
                .setMainOrderNo(mainOrderNo)
                .setOrderId(form.getOrderId())
                .setOrderNo(orderNo)
                .setSupplierCode(driverInfoLink.getSupplierCode())
                .setSupplierName(driverInfoLink.getSupplierName())
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
                .getDriverOrderInfoByStatus(driverId, null);

        //订单数目分组
        Map<String, Integer> map = new HashMap<>();
        //统计待接单
        List<String> orderNos = new ArrayList<>();
        for (DriverOrderInfo driverOrderInfo : driverOrderInfos) {
            map.merge(driverOrderInfo.getStatus(), 1, Integer::sum);
            orderNos.add(driverOrderInfo.getOrderNo());
        }
        //获取待接单数量
        ApiResult result = this.tmsClient.getDriverOrderTransportDetailById(driverId, orderNos);

        //重组数据
        Map<String, Object> response = new HashMap<>();
        Integer transitNum = map.get(DriverOrderStatusEnum.IN_TRANSIT.getCode());
        Integer finishedNum = map.get(DriverOrderStatusEnum.FINISHED.getCode());
        response.put("pending", result.getData() == null ? 0 : result.getData());
        response.put("transitNum", transitNum == null ? 0 : transitNum);
        response.put("finishedNum", finishedNum == null ? 0 : finishedNum);

        return CommonResult.success(response);
    }

    @ApiOperation(value = "查询反馈状态,orderNo=订单编号")
    @PostMapping(value = "/getFeedbackStatus")
    public CommonResult getFeedbackStatus(@RequestBody Map<String, String> map) {
        String orderNo = map.get("orderNo");
        if (org.apache.commons.lang.StringUtils.isEmpty(orderNo)) {
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        List<Map<String, Object>> process = this.getProcess(orderNo, false, new HashMap<>());
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
        if (this.driverOrderInfoService.isExistOrder(form.getOrderId())) {
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
        List<Map<String, Object>> process = this.getProcess(orderNo, true, cacheValue);
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
                form.setCmd(CommonConstant.CAR_GO_CUSTOMS);
                break;
            case 3:
                form.setCmd(CommonConstant.CAR_SEND);
                break;
            case 4:
                if (MapUtil.getInt(cacheValue, "deliveryAddressNum") > 1) {
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
        //查询送货地址
        ApiResult<List<DriverOrderTakeAdrVO>> result = this.tmsClient.getDriverOrderTakeAdrByOrderNo(Arrays.asList(orderNo), 2);
        if (!result.isOk()) {
            log.warn("查询送货地址失败");
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        List<DriverOrderTakeAdrVO> orderTakeAdrVOs = result.getData();
        Map<String, Object> response = new HashMap<>();
        if (orderTakeAdrVOs.size() == 1) { //只有一个送货地址，取送货地址
            String address = orderTakeAdrVOs.get(0).getAddress();
            response.put("address", address);
            return CommonResult.success(response);
        }
        //送货地址大于一个，取中转仓地址
        //查询派车单
        result = this.tmsClient.getOrderSendCarsByOrderNo(orderNo);
        if (!result.isOk()) {
            log.warn("查询派车单信息失败");
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(result.getData()));
        Long warehouseInfoId = json.getLong("warehouseInfoId");

        WarehouseInfo warehouseInfo = this.warehouseInfoService.getById(warehouseInfoId);
        //查询中转仓地址名称
        Collection<RegionCity> regionCities = regionCityService.listByIds(Arrays.asList(warehouseInfo.getStateCode(), warehouseInfo.getCityCode(), warehouseInfo.getAreaCode()));
        //拼接地址
        StringBuilder sb = new StringBuilder();
        regionCities.forEach(tmp -> sb.append(tmp.getName()));
        response.put("address", sb.append(warehouseInfo.getAddress()));
        return CommonResult.success(response);
    }


    /**
     * 获取流程
     */
    private List<Map<String, Object>> getProcess(String orderNo, boolean isGetNot, Map<String, Object> cacheValue) {
        ApiResult resultOne = this.tmsClient.getOrderTransportStatus(orderNo);
        if (!resultOne.isOk()) {
            log.error("远程调用查询中港订单状态失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        //查询送货地址数量，判断是送到中转仓库（1个以上），还是目的地（一个）
        ApiResult resultTwo = this.tmsClient.getDeliveryAddressNum(orderNo);
        if (!resultTwo.isOk()) {
            log.error("远程调用查询送货地址数量失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        cacheValue.put("deliveryAddressNum", resultTwo.getData());
        int num = Integer.parseInt(resultTwo.getData().toString());
        return DriverFeedbackStatusEnum.constructionProcess(resultOne.getData().toString(),
                num > 1 ? DriverFeedbackStatusEnum.THREE : null, isGetNot);
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


}
