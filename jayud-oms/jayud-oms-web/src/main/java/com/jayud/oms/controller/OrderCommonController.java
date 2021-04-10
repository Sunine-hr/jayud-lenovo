package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderOprCmdEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.enums.VehicleTypeEnum;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.po.ProductClassify;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.model.vo.cost.CostOrderDetailsVO;
import com.jayud.oms.service.ILogisticsTrackService;
import com.jayud.oms.service.IOrderInfoService;
import com.jayud.oms.service.IProductClassifyService;
import com.jayud.oms.service.IVehicleInfoService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @ApiOperation(value = "录入费用")
    @PostMapping(value = "/saveOrUpdateCost")
    public CommonResult saveOrUpdateCost(@RequestBody InputCostForm form) {
        if (form == null || form.getMainOrderId() == null) {
            return CommonResult.error(400, "参数不合法");
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

//
        result.put("mainOrderNo", mainOrderNo);
        result.put("subOrderNo", MapUtil.getStr(map, "orderNo"));
        result.put("goodsInfo", MapUtil.getStr(map, "goodsInfo"));
        result.put("vehicleSize", MapUtil.getStr(map, "vehicleSize"));
        result.put("customerName", MapUtil.getStr(map, "customerName"));
        result.put("isMainOrder", isMainOrder);
        result.put("isSea", isSea);
        result.put("cabinet", MapUtil.getStr(map, "cabinetTypeName") + "/" + MapUtil.getStr(map, "cabinetSizeName"));
        result.put("isTrailer", isTrailer);
        result.put("cabinetSizeName",MapUtil.getStr(map,"cabinetSizeName"));
        result.put("plateNumber",MapUtil.getStr(map,"plateNumber"));
        result.put("dateStr",MapUtil.getStr(map,"dateStr"));
        return CommonResult.success(result);
    }


    @ApiOperation(value = "费用审核")
    @PostMapping(value = "/auditCost")
    public CommonResult auditCost(@RequestBody AuditCostForm form) {
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

}

