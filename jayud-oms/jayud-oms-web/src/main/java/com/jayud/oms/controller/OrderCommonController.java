package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.entity.DataControl;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.excel.EasyExcelUtils;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.FinanceClient;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.enums.VehicleTypeEnum;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.model.vo.cost.CostOrderDetailsVO;
import com.jayud.oms.model.vo.worksheet.CostDetailsWorksheet;
import com.jayud.oms.model.vo.worksheet.TmsWorksheet;
import com.jayud.oms.service.*;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.jayud.common.utils.DateUtils.DATE_PATTERN;


@RestController
@RequestMapping("/orderCommon")
@Api(tags = "订单通用接口")
public class OrderCommonController {

    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private ILogisticsTrackService logisticsTrackService;
    @Autowired
    private IProductClassifyService productClassifyService;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private IVehicleInfoService vehicleInfoService;
    @Autowired
    private TmsClient tmsClient;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IProductBizService productBizService;
    @Autowired
    private IOrderReceivableCostService receivableCostService;
    @Autowired
    private IOrderPaymentCostService paymentCostService;
    @Autowired
    private ICostInfoService costInfoService;
    @Autowired
    private ICurrencyInfoService currencyInfoService;
    @Autowired
    private FinanceClient financeClient;


    @Value("${worksheet.tms}")
    private String worksheetTms;


    @ApiOperation(value = "录入费用")
    @PostMapping(value = "/saveOrUpdateCost")
    public CommonResult saveOrUpdateCost(@RequestBody InputCostForm form) {
        if (form == null || form.getMainOrderId() == null) {
            return CommonResult.error(400, "参数不合法");
        }
        OrderInfo orderInfo = this.orderInfoService.getById(form.getMainOrderId());
        //检查是否锁单区间
        if (this.financeClient.checkLockingInterval(1, DateUtils.LocalDateTime2Str(orderInfo.getOperationTime(), DATE_PATTERN), 2).getData()) {
            return CommonResult.error(400, "应付费用已锁,不允许修改,请联系财务");
        }
        if (this.financeClient.checkLockingInterval(0, DateUtils.LocalDateTime2Str(orderInfo.getOperationTime(), DATE_PATTERN), 2).getData()) {
            return CommonResult.error(400, "应收费用已锁,不允许修改,请联系财务");
        }

        if ("preSubmit_sub".equals(form.getCmd()) || "preSubmit_sub".equals(form.getCmd())) {
            if (form.getPaymentCostList() == null ||
                    form.getReceivableCostList() == null || form.getReceivableCostList().size() == 0 ||
                    form.getPaymentCostList().size() == 0) {
                return CommonResult.error(400, "参数不合法");
            }
        }

        if ("preSubmit_sub".equals(form.getCmd()) || "submit_sub".equals(form.getCmd())) {
            if (StringUtil.isNullOrEmpty(form.getOrderNo()) || StringUtil.isNullOrEmpty(form.getSubLegalName()) ||
                    StringUtil.isNullOrEmpty(form.getSubUnitCode())) {
                return CommonResult.error(400, "参数不合法");
            }
        }

        if ("preSubmit_main".equals(form.getCmd()) || "preSubmit_sub".equals(form.getCmd())) {
            List<InputPaymentCostForm> paymentCostForms = form.getPaymentCostList();
            List<InputReceivableCostForm> receivableCostForms = form.getReceivableCostList();
            for (InputPaymentCostForm paymentCost : paymentCostForms) {
                paymentCost.checkParam();
            }
            for (InputReceivableCostForm receivableCost : receivableCostForms) {
                receivableCost.checkUnitParam();
            }
        }

        if ("submit_main".equals(form.getCmd()) || "submit_sub".equals(form.getCmd())) {
            List<InputPaymentCostForm> paymentCostForms = form.getPaymentCostList();
            List<InputReceivableCostForm> receivableCostForms = form.getReceivableCostList();
            Map<String, String> costInfoMap = this.costInfoService.findCostInfo().stream().collect(Collectors.toMap(e -> e.getIdCode(), e -> e.getName()));
            for (InputPaymentCostForm paymentCost : paymentCostForms) {
                if (StringUtil.isNullOrEmpty(paymentCost.getCustomerCode())
                        || StringUtil.isNullOrEmpty(paymentCost.getCostCode())
                        || paymentCost.getCostTypeId() == null || paymentCost.getCostGenreId() == null
                        || StringUtil.isNullOrEmpty(paymentCost.getUnit())
                        || paymentCost.getUnitPrice() == null || paymentCost.getNumber() == null
                        || StringUtil.isNullOrEmpty(paymentCost.getCurrencyCode())
                        || paymentCost.getAmount() == null || paymentCost.getExchangeRate() == null
                        || paymentCost.getChangeAmount() == null) {
                    return CommonResult.error(400, "参数不合法");
                }
                if (paymentCost.getAmount().compareTo(new BigDecimal(0)) == 0) {
                    return CommonResult.error(400, costInfoMap.get(paymentCost.getCostCode()) + "应付金额不能为0");
                }

                paymentCost.checkParam();
            }
            for (InputReceivableCostForm receivableCost : receivableCostForms) {
                if (StringUtil.isNullOrEmpty(receivableCost.getCustomerCode())
                        || StringUtil.isNullOrEmpty(receivableCost.getCostCode())
                        || receivableCost.getCostTypeId() == null || receivableCost.getCostGenreId() == null
                        || StringUtil.isNullOrEmpty(receivableCost.getUnit())
                        || receivableCost.getUnitPrice() == null || receivableCost.getNumber() == null
                        || StringUtil.isNullOrEmpty(receivableCost.getCurrencyCode())
                        || receivableCost.getAmount() == null || receivableCost.getExchangeRate() == null
                        || receivableCost.getChangeAmount() == null) {
                    return CommonResult.error(400, "参数不合法");
                }
                if (receivableCost.getAmount().compareTo(new BigDecimal(0)) == 0) {
                    return CommonResult.error(400, costInfoMap.get(receivableCost.getCostCode()) + "应收金额不能为0");
                }
                receivableCost.checkUnitParam();
            }
        }
        //1.需求为，提交审核按钮跟在每一条记录后面 2.暂存是保存所有未提交审核的数据  3.提交审核的数据不可编辑和删除
        boolean result = orderInfoService.saveOrUpdateCost(form);
        if (!result) {
            return CommonResult.error(400, "调用失败");
        }
        return CommonResult.success();
    }


    @ApiOperation(value = "费用详情")
    @PostMapping(value = "/getCostDetail")
    public CommonResult<InputCostVO> getCostDetail(@RequestBody @Valid GetCostDetailForm form) {
        if (OrderOprCmdEnum.SUB_COST.getCode().equals(form.getCmd()) || OrderOprCmdEnum.SUB_COST_AUDIT.getCode().equals(form.getCmd())) {
            if (StringUtil.isNullOrEmpty(form.getSubOrderNo())) {
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            }
        }
        InputCostVO inputCostVO = orderInfoService.getCostDetail(form);
        return CommonResult.success(inputCostVO);
    }

    @ApiOperation(value = "获取费用详情子订单信息")
    @PostMapping(value = "/getCostDetailSubOrderInfo")
    public CommonResult<Map<String, Object>> subOrderFeeDetails(@RequestBody Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        String mainOrderNo = MapUtil.getStr(map, "mainOrderNo"); //获取子订单的主订单号
        Boolean isMainOrder = false;
        if (mainOrderNo == null) {
            isMainOrder = true;
            mainOrderNo = MapUtil.getStr(map, "orderNo");
        }

        String classCode = MapUtil.getStr(map, "classCode"); //获取子订单的主订单号
        Boolean isSea = false;
        if (OrderStatusEnum.HY.getCode().equals(classCode)) {
            isSea = true;
        }
        Boolean isTrailer = false;
        if (OrderStatusEnum.TC.getCode().equals(classCode)) {
            isTrailer = true;
        }
        Boolean isFast = false;
        if (OrderStatusEnum.CC.getCode().equals(classCode)) {
            isFast = true;
        }

        //中港订单列表
        String mark = MapUtil.getStr(map, "mark");
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(mark)) {
            result.put("licensePlate", MapUtil.getStr(map, "plateNumber"));
            result.put("takeTimeStr", MapUtil.getStr(map, "takeTimeStr"));
        }
        //内陆
        if (SubOrderSignEnum.NL.getSignOne().equals(mark)) {
            result.put("licensePlate", MapUtil.getStr(map, "licensePlate"));
            result.put("takeTimeStr", MapUtil.getStr(map, "deliveryDate"));
        }

        result.put("mainOrderNo", mainOrderNo);
        result.put("subOrderNo", MapUtil.getStr(map, "orderNo"));
        result.put("goodsInfo", MapUtil.getStr(map, "goodsInfo"));
        result.put("vehicleSize", MapUtil.getStr(map, "vehicleSize"));
        result.put("customerName", MapUtil.getStr(map, "customerName"));
        result.put("isMainOrder", isMainOrder);
        result.put("isSea", isSea);
        result.put("cabinet", MapUtil.getStr(map, "cabinetTypeName") + "/" + MapUtil.getStr(map, "cabinetSizeName"));
        result.put("isTrailer", isTrailer);
        result.put("cabinetSizeName", MapUtil.getStr(map, "cabinetSizeName"));
        result.put("licensePlate", MapUtil.getStr(map, "plateNumber"));
        result.put("isFast", isFast);

        result.put("takeTimeStr", MapUtil.getStr(map, "dateStr"));
        return CommonResult.success(result);
    }


    @ApiOperation(value = "费用审核")
    @PostMapping(value = "/auditCost")
    public CommonResult auditCost(@RequestBody AuditCostForm form) {

//        form.getReceivableCosts().size()>0
//        this.orderInfoService.getById(form.)
//        检查是否锁单区间
//        if (this.financeClient.checkLockingInterval(1, DateUtils.LocalDateTime2Str(orderInfo.getOperationTime(), DATE_PATTERN), 2).getData()) {
//            return CommonResult.error(400, "应付费用已锁,不允许修改,请联系财务");
//        }
//        if (this.financeClient.checkLockingInterval(0, DateUtils.LocalDateTime2Str(orderInfo.getOperationTime(), DATE_PATTERN), 2).getData()) {
//            return CommonResult.error(400, "应收费用已锁,不允许修改,请联系财务");
//        }

        if (form.getStatus() == null || "".equals(form.getStatus()) || !("3".equals(form.getStatus()) ||
                "0".equals(form.getStatus())) || form.getPaymentCosts() == null ||
                form.getReceivableCosts() == null) {
            return CommonResult.error(400, "参数不合法");
        }
        if ((form.getPaymentCosts().size() + form.getReceivableCosts().size()) == 0) {
            return CommonResult.error(400, "参数不合法");
        }
        for (OrderPaymentCost paymentCost : form.getPaymentCosts()) {
            if (paymentCost.getId() == null || "".equals(paymentCost.getId())) {
                return CommonResult.error(400, "参数不合法");
            }
        }
        for (OrderReceivableCost receivableCost : form.getReceivableCosts()) {
            if (receivableCost.getId() == null || "".equals(receivableCost.getId())) {
                return CommonResult.error(400, "参数不合法");
            }
        }
        boolean result = orderInfoService.auditCost(form);
        if (!result) {
            return CommonResult.error(400, "调用失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "反馈状态列表")
    @PostMapping(value = "/findReplyStatus")
    public CommonResult<List<LogisticsTrackVO>> findReplyStatus(@RequestBody @Valid QueryLogisticsTrackForm form) {
        List<LogisticsTrackVO> logisticsTrackVOS = logisticsTrackService.findReplyStatus(form);
        return CommonResult.success(logisticsTrackVOS);
    }

    @ApiOperation(value = "反馈状态确认")
    @PostMapping(value = "/confirmReplyStatus")
    public CommonResult confirmReplyStatus(@RequestBody LogisticsTrackForm form) {
        if (form == null || form.getLogisticsTrackForms() == null || form.getLogisticsTrackForms().size() == 0) {
            return CommonResult.error(400, "参数不合法");
        }
        List<LogisticsTrack> logisticsTracks = new ArrayList<>();
        for (LogisticsTrackVO logisticsTrack : form.getLogisticsTrackForms()) {
            LogisticsTrack track = new LogisticsTrack();
            track.setMainOrderId(form.getMainOrderId());
            track.setOrderId(form.getOrderId());
            track.setId(logisticsTrack.getId());
            track.setStatus(logisticsTrack.getStatus());
            track.setStatusName(logisticsTrack.getStatusName());
            track.setDescription(logisticsTrack.getDescription());
            track.setOperatorUser(logisticsTrack.getOperatorUser());
            track.setOperatorTime(DateUtils.str2LocalDateTime(logisticsTrack.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
            //处理附件
            List<FileView> fileViews = logisticsTrack.getFileViewList();
            track.setStatusPic(StringUtils.getFileStr(fileViews));
            track.setStatusPicName(StringUtils.getFileNameStr(fileViews));
            logisticsTracks.add(track);
        }
        if (logisticsTracks.size() > 0) {
            logisticsTrackService.saveOrUpdateBatch(logisticsTracks);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "创建订单界面获取业务类型 classCode=订单类型")
    @PostMapping(value = "/findCreateOrderClass")
    public CommonResult findCreateOrderClass(@RequestBody Map<String, Object> param) {
        String prePath = fileClient.getBaseUrl().getData().toString();
        String classCode = MapUtil.getStr(param, "classCode");
        List<ProductClassify> productClassifys = productClassifyService.findProductClassify(new HashMap<>());
        List<ProductClassifyVO> productClassifyVOS = new ArrayList<>();
        productClassifys.forEach(x -> {
            ProductClassifyVO productClassifyVO = new ProductClassifyVO();
            productClassifyVO.setId(x.getId());
            productClassifyVO.setFId(x.getFId());
            productClassifyVO.setIdCode(x.getIdCode());
            productClassifyVO.setName(x.getName());
            productClassifyVO.setIsOptional(x.getIsOptional());
            productClassifyVO.setObviousPic(prePath + x.getObviousPic());
            productClassifyVO.setVaguePic(prePath + x.getVaguePic());
            if (x.getFId() == 0) {
                List<ProductClassifyVO> subObjects = new ArrayList<>();
                productClassifys.forEach(v -> {
                    if (v.getFId().equals(x.getId())) {
                        ProductClassifyVO subObject = new ProductClassifyVO();
                        subObject.setId(v.getId());
                        subObject.setFId(v.getFId());
                        subObject.setIdCode(v.getIdCode());
                        subObject.setName(v.getName());
                        //处理步骤描述
                        String desc = v.getDescription();
                        if (desc != null) {
                            String[] descs = desc.split(";");
                            subObject.setDescs(descs);
                        }
                        subObjects.add(subObject);
                    }
                });
                productClassifyVO.setProductClassifyVOS(subObjects);
                productClassifyVOS.add(productClassifyVO);
            }
        });
        if (classCode != null && !"".equals(classCode)) {
            List<ProductClassifyVO> finalProductClassify = new ArrayList<>();
            for (ProductClassifyVO productClass : productClassifyVOS) {
                if (classCode.equals(productClass.getIdCode())) {
                    finalProductClassify.add(productClass);
                }
            }
            return CommonResult.success(finalProductClassify);
        } else {
            return CommonResult.success(productClassifyVOS);
        }
    }

    @ApiOperation(value = "获取当前时间")
    @PostMapping(value = "/getNowTime")
    public CommonResult getNowTime() {
        String nowTime = DateUtils.getLocalToStr(LocalDateTime.now());
        return CommonResult.success(nowTime);
    }


    @ApiOperation(value = "初始化车型尺寸,区分车型")
    @PostMapping(value = "/initVehicleSize")
    public CommonResult<InitVehicleSizeInfoVO> initVehicleSize() {
        InitVehicleSizeInfoVO initVehicleSizeInfoVO = new InitVehicleSizeInfoVO();
        //柜车尺寸集合
        List<VehicleSizeInfoVO> cabinetCars = new ArrayList<>();
        //吨车尺寸集合
        List<VehicleSizeInfoVO> tonCars = new ArrayList<>();
        List<VehicleSizeInfoVO> vehicleSizeInfoVOS = vehicleInfoService.findVehicleSize();
        for (VehicleSizeInfoVO obj : vehicleSizeInfoVOS) {
            if (VehicleTypeEnum.CABINET_CAR.getCode() == obj.getVehicleType()) {
                cabinetCars.add(obj);
            } else if (VehicleTypeEnum.TON_CAR.getCode() == obj.getVehicleType()) {
                tonCars.add(obj);
            }
        }
        initVehicleSizeInfoVO.setCabinetCars(cabinetCars);
        initVehicleSizeInfoVO.setTonCars(tonCars);
        return CommonResult.success(initVehicleSizeInfoVO);
    }


    @ApiOperation(value = "根据车类型初始化车型尺寸,区分车型")
    @PostMapping(value = "/initVehicleSizeByType")
    public CommonResult<List<VehicleSizeInfoVO>> initVehicleSizeByType(@RequestBody Map<String, Object> map) {
        Integer type = MapUtil.getInt(map, "type");
        List<VehicleSizeInfoVO> list = vehicleInfoService.findVehicleSize();
        List<VehicleSizeInfoVO> datas = list.stream().filter(e -> type == null || e.getVehicleType().equals(type)).collect(Collectors.toList());
        return CommonResult.success(datas);
    }

    @ApiOperation(value = "下拉选择卸货地址")
    @PostMapping(value = "/initTakeAdrBySubOrderNo")
    public CommonResult getCostOrderDetails(@RequestBody Map<String, Object> map) {

        String subOrderNo = MapUtil.getStr(map, "subOrderNo");
        String orderType = MapUtil.getStr(map, "orderType");
        if (StringUtils.isEmpty(subOrderNo) || StringUtils.isEmpty(orderType)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Object data = null;
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(orderType)) {
            data = this.tmsClient.initTakeAdrBySubOrderNo(subOrderNo).getData();
        }
        return CommonResult.success(data);
    }


    @ApiOperation(value = "录用费用页面(查询子订单详情)")
    @PostMapping("/getCostOrderDetail")
    public CommonResult<CostOrderDetailsVO> getCostOrderDetail(@RequestBody @Valid GetOrderDetailForm form) {
        InputOrderVO inputOrderVO = orderInfoService.getOrderDetail(form);
        CostOrderDetailsVO costOrderDetailsVO = new CostOrderDetailsVO();
        costOrderDetailsVO.assemblyData(inputOrderVO);
        return CommonResult.success(costOrderDetailsVO);
    }

    @ApiOperation(value = "供应商录入费用")
    @PostMapping(value = "/supplierEntryFee")
    public CommonResult supplierEntryFee(@RequestBody InputCostForm form) {
//        form.getPaymentCostList().forEach(e -> e.setUnit(UnitEnum.PCS.getCode()));
        form.checkSupplierEntryFee();
        this.orderInfoService.doSupplierEntryFee(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "获取供应商费用详情")
    @PostMapping(value = "/getSupplierCostDetail")
    public CommonResult<InputCostVO> getSupplierCostDetail(@RequestBody @Valid GetCostDetailForm form) {
        form.checkQuerySubOrderCost();
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), UserTypeEnum.SUPPLIER_TYPE.getCode()).getData();
        InputCostVO inputCostVO = new InputCostVO();
        //查询供应商费用
        if (!CollectionUtils.isEmpty(dataControl.getCompanyIds())) {
            form.setSupplierId(dataControl.getCompanyIds().get(0));
            inputCostVO = orderInfoService.getPayCostDetail(form);
        }
        return CommonResult.success(inputCostVO);
    }

    @ApiOperation(value = "获取供应商异常费用详情")
    @PostMapping(value = "/getSupplierAbnormalCostDetail")
    public CommonResult<InputCostVO> getSupplierAbnormalCostDetail(@RequestBody @Valid GetCostDetailForm form) {
        form.checkQuerySubOrderCost();
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), UserTypeEnum.SUPPLIER_TYPE.getCode()).getData();
        InputCostVO inputCostVO = new InputCostVO();
        //查询供应商费用
        if (CollectionUtils.isEmpty(dataControl.getCompanyIds())) {
            form.setSupplierId(dataControl.getCompanyIds().get(0));
            inputCostVO = orderInfoService.getSupplierAbnormalCostDetail(form);
        }
        return CommonResult.success(inputCostVO);
    }

//    @ApiOperation(value = "获取供应商默认录用费用值")
//    @PostMapping("/getDefaultSupplierCostValue")
//    public CommonResult<Map<String, Object>> getDefaultSupplierCostValue(@RequestBody Map<String, Object> map) {
//        String cmd = MapUtil.getStr(map, "cmd");
//        if (StringUtils.isEmpty(cmd)) {
//            return CommonResult.error(ResultEnum.PARAM_ERROR);
//        }
//        Map<String, Object> response = new HashMap<>();
//        if (SubOrderSignEnum.ZGYS.getSignOne().equals(cmd)) {
//            String currencyCodes = MapUtil.getStr(map, "currencyCodes");
//            if (StringUtils.isEmpty(currencyCodes)) return CommonResult.error(ResultEnum.PARAM_ERROR);
//            JSONArray currencyCodesArray = new JSONArray(currencyCodes);
//            response.put("isDefaultCurrency", false);
//            for (int i = 0; i < currencyCodesArray.size(); i++) {
//                JSONObject jsonObject = currencyCodesArray.getJSONObject(i);
//                if ("HKD".equals(jsonObject.getStr("code"))) {
//                    response.put("currencyCode", "HKD");
//                    response.put("isDefaultCurrency", true);
//                }
//            }
//
//        }
//        return CommonResult.success(response);
//    }

    @ApiOperation(value = "获取供应待处理操作")
    @PostMapping(value = "/getSupplyPendingOpt")
    public CommonResult<InputCostVO> getSupplyPendingOpt() {
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), UserTypeEnum.SUPPLIER_TYPE.getCode()).getData();
        InputCostVO inputCostVO = new InputCostVO();
        //查询供应商费用
        if (CollectionUtils.isEmpty(dataControl.getCompanyIds())) {
//            List<Map<String, Object>> list = this.orderInfoService.getSupplyPendingOpt(dataControl);
        }
        return CommonResult.success(inputCostVO);
    }


    @ApiOperation(value = "工作表")
    @GetMapping(value = "/worksheet")
    public void worksheet(@RequestParam("mainOrderId") Long mainOrderId,
                          @RequestParam("classCode") String classCode,
                          @RequestParam("isMainEntrance") String isMainEntrance,
                          HttpServletResponse response) {
        if (StringUtils.isEmpty(isMainEntrance)
                || StringUtils.isEmpty(classCode) || mainOrderId == null) {
            throw new JayudBizException(ResultEnum.PARAM_ERROR);
        }
        GetOrderDetailForm form = new GetOrderDetailForm();
        form.setClassCode(classCode);
        form.setMainOrderId(mainOrderId);
        InputOrderVO orderDetail = this.orderInfoService.getOrderDetail(form);
        TmsWorksheet tmsWorksheet = new TmsWorksheet().assemblyData(orderDetail);
        //费用明细
        Map<String, String> costInfoMap = this.costInfoService.findCostInfo().stream().collect(Collectors.toMap(CostInfo::getIdCode,
                CostInfo::getName));
        //币种
        Map<String, String> currencyMap = this.currencyInfoService.initCurrencyInfo().stream().collect(Collectors.toMap(InitComboxStrVO::getCode, InitComboxStrVO::getName));

        if ("main".equals(isMainEntrance)) {
            //查询所有应收费用
            List<OrderReceivableCost> receivableCosts = this.receivableCostService.getByMainOrderNo(tmsWorksheet.getMainOrderNo(), true,
                    Collections.singletonList(OrderStatusEnum.COST_1.getCode()));
            //补充充客户信息
            this.receivableCostService.supplyCustomerInfo(receivableCosts);
            List<OrderPaymentCost> paymentCosts = this.paymentCostService.getMainOrderCost(tmsWorksheet.getMainOrderNo(),
                    Arrays.asList(OrderStatusEnum.COST_0.getCode(), OrderStatusEnum.COST_2.getCode(), OrderStatusEnum.COST_3.getCode()));
            //补充供应商信息
            this.paymentCostService.supplySupplierInfo(paymentCosts);
            tmsWorksheet.assemblyCost(receivableCosts, paymentCosts, costInfoMap, currencyMap);
        } else {
            //查询所有应收费用
            List<OrderReceivableCost> receivableCosts = this.receivableCostService.getByMainOrderNo(tmsWorksheet.getMainOrderNo(), false,
                    Collections.singletonList(OrderStatusEnum.COST_1.getCode()));
            List<OrderPaymentCost> paymentCosts = this.paymentCostService.getSubCostByMainOrderNo(new OrderPaymentCost().setMainOrderNo(tmsWorksheet.getMainOrderNo()).setSubType(SubOrderSignEnum.ZGYS.getSignOne()),
                    Collections.singletonList(OrderStatusEnum.COST_1.getCode()));
            //补充供应商信息
            this.paymentCostService.supplySupplierInfo(paymentCosts);
            tmsWorksheet.assemblyCost(receivableCosts, paymentCosts, costInfoMap, currencyMap);
        }
        Map<String, List<CostDetailsWorksheet>> costMap = new HashMap<>();
        costMap.put("re", tmsWorksheet.getReCostDetails());
        costMap.put("pay", tmsWorksheet.getPayCostDetails());
        EasyExcelUtils.fillTemplate(new JSONObject(tmsWorksheet), costMap,
                worksheetTms, null, "工作表", response);

    }

}

