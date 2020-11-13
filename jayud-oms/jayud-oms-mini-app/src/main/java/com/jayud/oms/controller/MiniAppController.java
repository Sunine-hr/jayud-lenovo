package com.jayud.oms.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.model.bo.AddDriverEmploymentFeeForm;
import com.jayud.oms.model.bo.AddDriverOrderTransportForm;
import com.jayud.oms.model.bo.QueryDriverOrderTransportForm;
import com.jayud.oms.model.enums.DriverOrderStatusEnum;
import com.jayud.oms.model.enums.EmploymentFeeStatusEnum;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.po.DriverEmploymentFee;
import com.jayud.oms.model.po.DriverOrderInfo;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.security.util.SecurityUtil;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PostMapping("/getDriverOrderTransport")
    @ApiOperation(value = "查看司机中港订单")
    public CommonResult getDriverOrderTransport(@Valid @RequestBody QueryDriverOrderTransportForm form) {

        form.setDriverId(Long.valueOf(SecurityUtil.getUserInfo()));
        String status = form.getStatus();
        if (DriverOrderStatusEnum.ALL.getCode().equals(form.getStatus())) {
            //查询所有订单
            status = null;
        }
        //根据状态查询司机接单信息，如果没有代表还有没有接单
        List<DriverOrderInfo> driverOrderInfos = this.driverOrderInfoService.getDriverOrderInfoByStatus(form.getDriverId(), status);

        List<Long> orderIds = driverOrderInfos.stream().map(DriverOrderInfo::getOrderId).collect(Collectors.toList());
        //组装订单
        form.assemblyOrder(orderIds);

        //查全部，要已经接单数据带过去

        //查询待接单,把已经接单订单带到排除id集合里面
        //查询
        //在首页，先查询司机接单表信息，看是否存在数据，把已经接单信息排除掉，剩下就是待接单信息，
        // 要传待接单状态，然后进行转换状态去查询
        Object data = tmsClient.getDriverOrderTransport(form).getData();
        List<DriverOrderTransportVO> tmp = null;
        if (data != null) {
            tmp = (List<DriverOrderTransportVO>) data;
        }
        return CommonResult.success(tmp);
    }

    @PostMapping("/confirmOrderReceiving")
    @ApiOperation(value = "司机确认接单")
    public CommonResult confirmOrderReceiving(@Valid @RequestBody AddDriverOrderTransportForm form) {
        DriverOrderInfo driverOrderInfo = new DriverOrderInfo()
                .setDriverId(Long.valueOf(SecurityUtil.getUserInfo()))
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
    public CommonResult initEmploymentFeeBox() {
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
        Map<String, Object> map = new HashMap<>();
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
        String orderId = map.get("orderId");
        String orderNo = map.get("orderNo");
        if (org.apache.commons.lang.StringUtils.isEmpty(orderId) ||
                org.apache.commons.lang.StringUtils.isEmpty(orderNo)) {
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //查询是否存在录用费用项
        if (!this.driverEmploymentFeeService.isExist(driverId,
                Long.parseLong(orderId), EmploymentFeeStatusEnum.SUBMIT.getCode())) {
            return CommonResult.error(400, "不存在录用费用项,不能费用提交");
        }
        //查询所有提交的费用项
        List<DriverEmploymentFee> employmentFee = this.driverEmploymentFeeService.getEmploymentFee(orderNo,
                driverId, EmploymentFeeStatusEnum.SUBMIT.getCode());
        //批量费用提交
        this.driverEmploymentFeeService.feeSubmission(employmentFee);
        return CommonResult.success();
    }

    /**
     * 查询司机的中港订单详情
     */
    @PostMapping("/getDriverOrderTransportDetail")
    @ApiOperation(value = "根据订单编号查询司机的中港订单详情 orderNo=订单编号,orderId=订单id")
    public CommonResult getDriverOrderTransportDetail(@RequestBody Map<String, String> map) {
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
            driverOrderTransportVO.setTakeOrders(false);
            return CommonResult.success(driverOrderTransportVO);
        } else {
            driverOrderTransportVO.setTakeOrders(true);
            driverOrderTransportVO.setIsFeeSubmitted(false);
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
}
