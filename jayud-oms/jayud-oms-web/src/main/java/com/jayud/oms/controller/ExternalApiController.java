package com.jayud.oms.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.DataControl;
import com.jayud.common.entity.MapEntity;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.entity.TreeNode;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.*;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.FinanceClient;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.enums.VehicleTypeEnum;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@RestController
@Api(tags = "oms对外接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    IOrderInfoService orderInfoService;
    @Autowired
    FileClient fileClient;
    @Autowired
    ILogisticsTrackService logisticsTrackService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    IAuditInfoService auditInfoService;
    @Autowired
    IWarehouseInfoService warehouseInfoService;
    @Autowired
    ISupplierInfoService supplierInfoService;
    @Autowired
    IDriverInfoService driverInfoService;
    @Autowired
    IOrderPaymentCostService paymentCostService;
    @Autowired
    IOrderReceivableCostService receivableCostService;
    @Autowired
    ICurrencyInfoService currencyInfoService;
    @Autowired
    private ICustomerInfoService customerInfoService;
    @Autowired
    ICostGenreService costGenreService;
    @Autowired
    private IPortInfoService portInfoService;
    @Autowired
    private IVehicleInfoService vehicleInfoService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IOrderAddressService orderAddressService;
    @Autowired
    private IOrderReceivableCostService orderReceivableCostService;
    @Autowired
    private IOrderPaymentCostService orderPaymentCostService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IOrderTypeNumberService orderTypeNumberService;
    @Autowired
    private IOrderFlowSheetService orderFlowSheetService;
    @Autowired
    private ICostCommonService costCommonService;
    @Autowired
    private ICostInfoService costInfoService;
    @Autowired
    private TmsClient tmsClient;
    @Autowired
    private IProductBizService productBizService;
    @Autowired
    private IProductClassifyService productClassifyService;
    @Autowired
    private IMsgPushRecordService msgPushRecordService;
    @Autowired
    private WechatMsgService wechatMsgService;
    @Autowired
    private ISystemConfService systemConfService;
    @Autowired
    private MapPositioningService mapPositioningService;
    @Autowired
    private GPSPositioningApiService gpsPositioningApiService;
    @Autowired
    private IGpsPositioningService gpsPositioningService;
    @Autowired
    private ICostGenreTaxRateService costGenreTaxRateService;
    @Autowired
    private IRegionCityService regionCityService;
    @Autowired
    private FinanceClient financeClient;
    @Autowired
    private IClientSecretKeyService clientSecretKeyService;
    @Autowired
    private ICostTypeService costTypeService;

    @ApiOperation(value = "保存主订单")
    @RequestMapping(value = "/api/oprMainOrder")
    public ApiResult oprMainOrder(@RequestBody InputMainOrderForm form) {
        String result = orderInfoService.oprMainOrder(form, null);
        if (result != null) {
            return ApiResult.ok(result);
        }
        return ApiResult.error();
    }

    /**
     * 暂存订单
     */
    @RequestMapping(value = "/api/holdOrder")
    public ApiResult holdOrder(@RequestBody InputOrderForm form) {
        form.setCmd(CommonConstant.PRE_SUBMIT);
        orderInfoService.createOrder(form);
        return ApiResult.ok();
    }


    @ApiOperation(value = "获取主订单信息")
    @RequestMapping(value = "/api/getMainOrderById")
    ApiResult getMainOrderById(@RequestParam(value = "id") Long id) {
        InputMainOrderVO inputOrderVO = orderInfoService.getMainOrderById(id);
        return ApiResult.ok(inputOrderVO);
    }

    @ApiOperation(value = "获取主订单ID")
    @RequestMapping(value = "/api/getIdByOrderNo")
    ApiResult getIdByOrderNo(@RequestParam(value = "orderNo") String orderNo) {
        Long mainOrderId = orderInfoService.getIdByOrderNo(orderNo);
        return ApiResult.ok(mainOrderId);
    }


    @ApiOperation(value = "保存操作状态")
    @RequestMapping(value = "/api/saveOprStatus")
    ApiResult saveOprStatus(@RequestBody OprStatusForm form) {
        LogisticsTrack logisticsTrack = new LogisticsTrack();
        logisticsTrack.setMainOrderId(form.getMainOrderId());
        logisticsTrack.setOrderId(form.getOrderId());
        logisticsTrack.setStatus(form.getStatus());
        logisticsTrack.setStatusName(form.getStatusName());
        logisticsTrack.setOperatorUser(form.getOperatorUser());
        LocalDateTime operatorTime = org.apache.commons.lang.StringUtils.isNotEmpty(form.getOperatorTime())
                ? DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN) : LocalDateTime.now();
        logisticsTrack.setOperatorTime(operatorTime);
        logisticsTrack.setStatusPic(form.getStatusPic());
        logisticsTrack.setStatusPicName(form.getStatusPicName());
        logisticsTrack.setDescription(form.getDescription());
        logisticsTrack.setEntrustNo(form.getEntrustNo());
        logisticsTrack.setGoCustomsTime(DateUtils.str2LocalDateTime(form.getGoCustomsTime(), DateUtils.DATE_TIME_PATTERN));
        logisticsTrack.setPreGoCustomsTime(DateUtils.str2LocalDateTime(form.getPreGoCustomsTime(), DateUtils.DATE_TIME_PATTERN));
        logisticsTrack.setCreatedUser(UserOperator.getToken());
        logisticsTrack.setCreatedTime(LocalDateTime.now());
        logisticsTrack.setType(form.getBusinessType()); //业务类型 BusinessTypeEnum类
        logisticsTrackService.saveOrUpdate(logisticsTrack);
        return ApiResult.ok();
    }

    @ApiOperation(value = "根据orderId和类型删除物流轨迹跟踪表")
    @RequestMapping(value = "/api/deleteLogisticsTrackByType")
    ApiResult deleteLogisticsTrackByType(@RequestParam("orderId") Long orderId, @RequestParam("type") Integer type) {
        this.logisticsTrackService.deleteLogisticsTrackByType(orderId, type);
        return ApiResult.ok();
    }

    /**
     * 子订单流程
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/handleSubProcess")
    ApiResult handleSubProcess(@RequestBody HandleSubProcessForm form) {
        List<OrderStatusVO> orderStatusVOS = orderInfoService.handleSubProcess(form);
        return ApiResult.ok(orderStatusVOS);
    }

    /**
     * 记录审核信息
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/saveAuditInfo")
    ApiResult<Boolean> saveAuditInfo(@RequestBody AuditInfoForm form) {
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setExtId(form.getExtId());
        auditInfo.setExtUniqueFlag(form.getExtUniqueFlag());//目前只有账单编号
        auditInfo.setExtDesc(form.getExtDesc());
        auditInfo.setAuditTypeDesc(form.getAuditTypeDesc());
        auditInfo.setAuditStatus(form.getAuditStatus());
        auditInfo.setAuditComment(form.getAuditComment());
        auditInfo.setCreatedUser(UserOperator.getToken());
        auditInfo.setAuditUser(form.getAuditUser());
        auditInfo.setStatusFile(StringUtils.getFileStr(form.getFileViews()));
        auditInfo.setStatusFileName(StringUtils.getFileNameStr(form.getFileViews()));
        auditInfo.setAuditTime(form.getAuditTime() == null ? LocalDateTime.now() : form.getAuditTime());
        auditInfo.setCreatedTime(LocalDateTime.now());
        boolean result = auditInfoService.save(auditInfo);
        return ApiResult.ok(result);
    }

    @ApiOperation(value = "中转仓库")
    @RequestMapping(value = "api/initWarehouseInfo")
    public CommonResult initWarehouseInfo() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, 1);
        List<WarehouseInfo> warehouseInfos = warehouseInfoService.list(queryWrapper);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (WarehouseInfo warehouseInfo : warehouseInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(warehouseInfo.getId());
            initComboxVO.setName(warehouseInfo.getWarehouseName());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "中转仓库")
    @RequestMapping(value = "api/initComboxWarehouseVO")
    public CommonResult initComboxWarehouseVO() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, 1);
        List<WarehouseInfo> warehouseInfos = warehouseInfoService.list(queryWrapper);
        List<InitComboxWarehouseVO> initComboxVOS = new ArrayList<>();
        for (WarehouseInfo warehouseInfo : warehouseInfos) {
            InitComboxWarehouseVO initComboxVO = new InitComboxWarehouseVO();
            initComboxVO.setId(warehouseInfo.getId());
            initComboxVO.setName(warehouseInfo.getWarehouseName());
            initComboxVO.setPhone(warehouseInfo.getContactNumber());
            initComboxVO.setAddress(warehouseInfo.getAddress());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "根据id集合获取中转仓库")
    @RequestMapping(value = "api/getWarehouseMapByIds")
    public ApiResult<Map<Long, WarehouseInfo>> getWarehouseMapByIds(@RequestParam("warehouseIds") List<Long> warehouseIds) {
        List<WarehouseInfo> warehouseInfos = this.warehouseInfoService.listByIds(warehouseIds);
        return ApiResult.ok(warehouseInfos.stream().collect(Collectors.toMap(WarehouseInfo::getId, e -> e)));
    }

    @ApiOperation(value = "下拉框:获取审核通过供应商")
    @RequestMapping(value = "api/initSupplierInfo")
    public CommonResult initSupplierInfo() {
        List<SupplierInfo> supplierInfos = supplierInfoService.getApprovedSupplier(
                BeanUtils.convertToFieldName(true,
                        SupplierInfo::getId, SupplierInfo::getSupplierChName));
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (SupplierInfo supplierInfo : supplierInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(supplierInfo.getId());
            initComboxVO.setName(supplierInfo.getSupplierChName());
            initComboxVOS.add(initComboxVO);
        }

        return CommonResult.success(initComboxVOS);
    }

    /**
     * 删除前面操作成功的记录
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "api/delOprStatus")
    ApiResult delOprStatus(@RequestParam("orderId") Long orderId) {
        logisticsTrackService.removeById(orderId);
        return ApiResult.ok();
    }

    /**
     * 删除特定单的操作流程
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/delSpecOprStatus")
    ApiResult delSpecOprStatus(@RequestBody DelOprStatusForm form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.ORDER_ID, form.getOrderId());
        queryWrapper.in(SqlConstant.STATUS, form.getStatus());
        logisticsTrackService.remove(queryWrapper);
        return ApiResult.ok();
    }

    @ApiOperation(value = "初始化司机下拉框")
    @RequestMapping(value = "api/initDriver")
    public CommonResult initDriver() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, 1);
        List<DriverInfo> driverInfos = driverInfoService.list(queryWrapper);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (DriverInfo driverInfo : driverInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(driverInfo.getId());
            initComboxVO.setName(driverInfo.getName());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "初始化车辆下拉框")
    @RequestMapping(value = "api/initVehicle")
    public ApiResult<List<InitComboxVO>> initVehicle(@RequestParam("type") Integer type) {
        List<VehicleInfo> vehicleInfos = vehicleInfoService
                .getByCondition(new VehicleInfo().setStatus(StatusEnum.ENABLE.getCode())
                        .setType(type));

        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (VehicleInfo vehicleInfo : vehicleInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(vehicleInfo.getId());
            initComboxVO.setName(vehicleInfo.getPlateNumber());
            initComboxVOS.add(initComboxVO);
        }
        return ApiResult.ok(initComboxVOS);
    }

    @ApiOperation(value = "初始化供应商车辆下拉框 type:车辆类型(0:中港车,1:内陆车)")
    @RequestMapping(value = "api/initVehicleBySupplier")
    public ApiResult<List<InitComboxStrVO>> initVehicleBySupplier(@RequestParam("supplierId") Long supplierId,
                                                                  @RequestParam("type") Integer type) {
        List<VehicleInfo> vehicleInfos = vehicleInfoService
                .getByCondition(new VehicleInfo().setSupplierId(supplierId)
                        .setStatus(StatusEnum.ENABLE.getCode())
                        .setType(type));

        List<InitComboxStrVO> initComboxVOS = new ArrayList<>();
        for (VehicleInfo vehicleInfo : vehicleInfos) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setId(vehicleInfo.getId());
            initComboxStrVO.setName(vehicleInfo.getPlateNumber());
            initComboxStrVO.setNote(vehicleInfo.getHkNumber());
            initComboxVOS.add(initComboxStrVO);
        }
        return ApiResult.ok(initComboxVOS);
    }

    @ApiOperation(value = "初始化车辆下拉框 (确认派车)")
    @RequestMapping(value = "api/initVehicleInfo")
    public ApiResult<VehicleInfoLinkVO> initVehicleInfo(@RequestParam("vehicleId") Long vehicleId) {
        VehicleDetailsVO vehicleDetailsVO = this.vehicleInfoService.getVehicleDetailsById(vehicleId);
        //组装数据
        VehicleInfoLinkVO tmp = new VehicleInfoLinkVO();
        tmp.setDriverInfos(vehicleDetailsVO.getDriverInfoVOS());
        tmp.setPlateNumber(vehicleDetailsVO.getPlateNumber());
        tmp.setHkNumber(vehicleDetailsVO.getHkNumber());
        tmp.setSupplierName(vehicleDetailsVO.getSupplierInfoVO().getSupplierChName());
        tmp.setSupplierId(vehicleDetailsVO.getSupplierId());
        tmp.setMainDriverId(vehicleDetailsVO.getMainDriverId());

        return ApiResult.ok(tmp);
    }

    @ApiOperation(value = "根据车辆id获取车辆和供应商信息")
    @RequestMapping(value = "api/getVehicleAndSupplierInfo")
    public ApiResult<VehicleInfoLinkVO> getVehicleAndSupplierInfo(@RequestParam("vehicleId") Long vehicleId) {
        VehicleDetailsVO vehicleDetailsVO = this.vehicleInfoService.getVehicleAndSupplierInfo(vehicleId);
        return ApiResult.ok(vehicleDetailsVO);
    }

    /**
     * 司机下拉框联动车辆供应商，大陆车牌，香港车牌，司机电话
     *
     * @return
     */
    @RequestMapping(value = "/api/initDriverInfo")
    ApiResult initDriverInfo(@RequestParam("driverId") Long driverId) {
        DriverInfoLinkVO driverInfo = driverInfoService.getDriverInfoLink(driverId);
        return ApiResult.ok(driverInfo);
    }


    /**
     * 应付/应收暂存
     */
    @RequestMapping(value = "/api/oprCostBill")
    ApiResult<Boolean> oprCostBill(@RequestBody OprCostBillForm form) {
        Boolean result = false;
        if ("payment".equals(form.getOprType())) {
            List<OrderPaymentCost> paymentCosts = new ArrayList<>();
            if (form.getCmd().contains("pre")) {//暂存应付
                List<OrderPaymentCost> delCosts = new ArrayList<>();
                for (Long costId : form.getCostIds()) {
                    OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                    orderPaymentCost.setId(costId);
                    orderPaymentCost.setIsBill("1");//暂存
                    paymentCosts.add(orderPaymentCost);
                }
                //获取现存数据有多少暂存的，改为未出账
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("is_bill", "1");
                List<OrderPaymentCost> existPaymentCosts = paymentCostService.list(queryWrapper);
                for (OrderPaymentCost existPaymentCost : existPaymentCosts) {
                    OrderPaymentCost delCost = new OrderPaymentCost();
                    delCost.setId(existPaymentCost.getId());
                    delCost.setIsBill("0");//未出账
                    delCosts.add(delCost);
                }
                //把原来暂存的清除,更新未出账
                if (delCosts.size() > 0) {
                    paymentCostService.updateBatchById(delCosts);
                }
            } else if ("del".equals(form.getCmd())) {//删除对账单
                for (Long costId : form.getCostIds()) {
                    OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                    orderPaymentCost.setId(costId);
                    orderPaymentCost.setIsBill("0");//未出账
                    paymentCosts.add(orderPaymentCost);
                }
            } else {//生成应付账单
                for (Long costId : form.getCostIds()) {
                    OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                    orderPaymentCost.setId(costId);
                    orderPaymentCost.setIsBill("2");//生成对账单
                    paymentCosts.add(orderPaymentCost);
                }
            }
            result = paymentCostService.updateBatchById(paymentCosts);
        } else if ("receivable".equals(form.getOprType())) {
            List<OrderReceivableCost> receivableCosts = new ArrayList<>();
            if (form.getCmd().contains("pre")) {//暂存应收
                List<OrderReceivableCost> delCosts = new ArrayList<>();
                for (Long costId : form.getCostIds()) {
                    OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                    orderReceivableCost.setId(costId);
                    orderReceivableCost.setIsBill("1");//暂存
                    receivableCosts.add(orderReceivableCost);
                }
                //获取现存数据有多少暂存的，改为未出账
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("is_bill", "1");
                List<OrderReceivableCost> existReceivableCosts = receivableCostService.list(queryWrapper);
                for (OrderReceivableCost existReceivableCost : existReceivableCosts) {
                    OrderReceivableCost delCost = new OrderReceivableCost();
                    delCost.setId(existReceivableCost.getId());
                    delCost.setIsBill("0");//未出账
                    delCosts.add(delCost);
                }
                //把原来暂存的清除,更新未出账
                if (delCosts.size() > 0) {
                    receivableCostService.updateBatchById(delCosts);
                }
            } else if ("del".equals(form.getCmd())) {//删除对账单
                for (Long costId : form.getCostIds()) {
                    OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                    orderReceivableCost.setId(costId);
                    orderReceivableCost.setIsBill("0");//未出账
                    receivableCosts.add(orderReceivableCost);
                }
            } else {//生成应收账单
                for (Long costId : form.getCostIds()) {
                    OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                    orderReceivableCost.setId(costId);
                    orderReceivableCost.setIsBill("2");//生成对账单
                    receivableCosts.add(orderReceivableCost);
                }
            }
            result = receivableCostService.updateBatchById(receivableCosts);
        }
        return ApiResult.ok(result);
    }

    @ApiOperation(value = "币种")
    @RequestMapping(value = "api/initCurrencyInfo")
    public ApiResult initCurrencyInfo() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, 1);
        List<CurrencyInfo> currencyInfos = currencyInfoService.list(queryWrapper);
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (CurrencyInfo currencyInfo : currencyInfos) {
            InitComboxStrVO initComboxVO = new InitComboxStrVO();
            initComboxVO.setCode(currencyInfo.getCurrencyCode());
            initComboxVO.setName(currencyInfo.getCurrencyName());
            initComboxVO.setId(currencyInfo.getId());
            initComboxStrVOS.add(initComboxVO);
        }
        return ApiResult.ok(initComboxStrVOS);
    }

    @ApiOperation(value = "币种")
    @RequestMapping(value = "api/initCurrencyInfo2")
    public ApiResult initCurrencyInfo2() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, 1);
        List<CurrencyInfo> currencyInfos = currencyInfoService.list(queryWrapper);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (CurrencyInfo currencyInfo : currencyInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(currencyInfo.getId());
            initComboxVO.setName(currencyInfo.getCurrencyName());
            initComboxVOS.add(initComboxVO);
        }
        return ApiResult.ok(initComboxVOS);
    }

    /**
     * 编辑保存确定
     *
     * @param costIds
     * @param oprType 应付还是应收
     * @return
     */
    @ApiOperation(value = "编辑保存确定")
    @RequestMapping(value = "api/editSaveConfirm")
    public ApiResult editSaveConfirm(@RequestParam("costIds") List<Long> costIds, @RequestParam("oprType") String oprType,
                                     @RequestParam("cmd") String cmd, @RequestBody Map<String, Object> param) {
        if ("save_confirm".equals(cmd)) {
            if ("receivable".equals(oprType)) {
                OrderReceivableCost receivableCost = new OrderReceivableCost();
                receivableCost.setIsBill("save_confirm")//持续操作中的过度状态
                        .setTmpBillNo(param.get("billNo").toString());//TODO 等待前端更改,需要前端传账单编号
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.in("id", costIds);
                receivableCostService.update(receivableCost, updateWrapper);
            } else if ("payment".equals(oprType)) {
                OrderPaymentCost paymentCost = new OrderPaymentCost();
                paymentCost.setIsBill("save_confirm")
                        .setTmpBillNo(param.get("billNo").toString());//TODO 等待前端更改,需要前端传账单编号
                ;//持续操作中的过度状态
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.in("id", costIds);
                paymentCostService.update(paymentCost, updateWrapper);
            }
        } else if ("edit_del".equals(cmd)) {
            if ("receivable".equals(oprType)) {
                OrderReceivableCost receivableCost = new OrderReceivableCost();
                receivableCost.setIsBill("0");//从save_confirm状态回滚到未出账-0状态
                receivableCost.setStatus(1);//草稿状态
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.in("id", costIds);
                receivableCostService.update(receivableCost, updateWrapper);
            } else if ("payment".equals(oprType)) {
                OrderPaymentCost paymentCost = new OrderPaymentCost();
                paymentCost.setIsBill("0");//从save_confirm状态回滚到未出账-0状态
                paymentCost.setStatus(1);//草稿状态
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.in("id", costIds);
                paymentCostService.update(paymentCost, updateWrapper);
            }
        }
        return ApiResult.ok(true);
    }

    /**
     * 提交财务审核时，财务可能编辑费用类型
     *
     * @param forms
     * @param cmd
     * @return
     */
    @RequestMapping(value = "api/oprCostGenreByCw")
    ApiResult<Boolean> oprCostGenreByCw(@RequestBody List<OrderCostForm> forms, @RequestParam("cmd") String cmd) {
        if ("receivable".equals(cmd)) {
            for (OrderCostForm orderCost : forms) {
                OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                orderReceivableCost.setId(orderCost.getCostId());
                orderReceivableCost.setCostGenreId(orderCost.getCostGenreId());
                orderReceivableCost.setOptName(orderCost.getLoginUserName());
                orderReceivableCost.setOptTime(LocalDateTime.now());
                receivableCostService.updateById(orderReceivableCost);
            }
        } else if ("payment".equals(cmd)) {
            for (OrderCostForm orderCost : forms) {
                OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                orderPaymentCost.setId(orderCost.getCostId());
                orderPaymentCost.setCostGenreId(orderCost.getCostGenreId());
                orderPaymentCost.setOptName(orderCost.getLoginUserName());
                orderPaymentCost.setOptTime(LocalDateTime.now());
                paymentCostService.updateById(orderPaymentCost);
            }
        }
        return ApiResult.ok(true);
    }

    /**
     * 开票审核通过之后，需要反推汇率和本币金额到费用录入表
     *
     * @param forms
     * @param cmd
     * @return
     */
    @RequestMapping(value = "api/writeBackCostData")
    ApiResult writeBackCostData(@RequestBody List<OrderCostForm> forms, @RequestParam("cmd") String cmd) {
        if ("receivable".equals(cmd)) {
            for (OrderCostForm orderCost : forms) {
                //获取该条费用以出账时结算币种的汇率和本币金额
//                InputReceivableCostVO sCost = receivableCostService.getWriteBackSCostData(orderCost.getCostId());
//                //汇率校验
//                if (sCost.getExchangeRate() == null || sCost.getExchangeRate().compareTo(new BigDecimal("0")) == 0) {
//                    return ApiResult.error(10001, "请配置原始币种:" + sCost.getOCurrencyName() + ",兑换币种:人民币的汇率");
//                }
                OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                orderReceivableCost.setId(orderCost.getCostId());
                orderReceivableCost.setExchangeRate(orderCost.getLocalMoneyRate());//汇率
                orderReceivableCost.setChangeAmount(orderCost.getLocalMoney());//本币金额
                orderReceivableCost.setOptName(orderCost.getLoginUserName());
                orderReceivableCost.setOptTime(LocalDateTime.now());
                receivableCostService.updateById(orderReceivableCost);
            }
        } else if ("payment".equals(cmd)) {
            for (OrderCostForm orderCost : forms) {
                //获取该条费用以出账时结算币种的汇率和本币金额
//                InputPaymentCostVO fCost = paymentCostService.getWriteBackFCostData(orderCost.getCostId());
//                //汇率校验
//                if (fCost.getExchangeRate() == null || fCost.getExchangeRate().compareTo(new BigDecimal("0")) == 0) {
//                    return ApiResult.error(10001, "请配置原始币种:" + fCost.getOCurrencyName() + ",兑换币种:人民币的汇率");
//                }
                OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                orderPaymentCost.setId(orderCost.getCostId());
                orderPaymentCost.setExchangeRate(orderCost.getLocalMoneyRate());//汇率
                orderPaymentCost.setChangeAmount(orderCost.getLocalMoney());//本币金额
                orderPaymentCost.setOptName(orderCost.getLoginUserName());
                orderPaymentCost.setOptTime(LocalDateTime.now());
                paymentCostService.updateById(orderPaymentCost);
            }
        }
        return ApiResult.ok();
    }

    /**
     * 获取所有可用的费用类型
     *
     * @return
     */
    @RequestMapping(value = "api/findEnableCostGenre")
    ApiResult<List<InitComboxVO>> findEnableCostGenre() {
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        List<CostGenre> costGenres = costGenreService.getEnableCostGenre();
        for (CostGenre costGenre : costGenres) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(costGenre.getId());
            initComboxVO.setName(costGenre.getName());
            initComboxVOS.add(initComboxVO);
        }
        return ApiResult.ok(initComboxVOS);
    }


    /**
     * 根据客户名称获取订单信息
     */
    @RequestMapping(value = "/api/mainOrder/getByCustomerName")
    public ApiResult<OrderInfo> getByCustomerName(@RequestParam("customerName") String customerName) {
        List<OrderInfo> orderList = this.orderInfoService.getByCustomerName(customerName);
        return ApiResult.ok(orderList);
    }

    /**
     * 根据客户名称获取订单信息
     */
    @RequestMapping(value = "/api/mainOrder/getCustomerIdByCustomerName")
    ApiResult getCustomerIdByCustomerName(@RequestParam("customerName") String customerName) {
        List<CustomerInfo> customerInfos = this.customerInfoService.getByCustomerName(customerName);
        return ApiResult.ok(customerInfos);
    }

    /**
     * 根据客户id查询客户信息
     */
    @RequestMapping(value = "/api/getCustomerInfoById")
    public ApiResult getCustomerInfoById(@RequestParam("id") Long id) {
        CustomerInfo customerInfo = this.customerInfoService.getById(id);
        return ApiResult.ok(customerInfo);
    }

    /**
     * 根据主订单集合查询主订单信息
     */
    @RequestMapping(value = "/api/mainOrder/getByOrderNos")
    public ApiResult getByOrderNos(@RequestParam("orderNos") List<String> orderNos) {
        List<OrderInfo> orders = this.orderInfoService.getByOrderNos(orderNos);
        return ApiResult.ok(orders);
    }

    /**
     * 查询物流轨迹节点
     */
    @RequestMapping(value = "/api/getLogisticsTrackNode")
    public ApiResult getLogisticsTrackNode(@RequestBody String condition) {
        LogisticsTrack logisticsTrack = new LogisticsTrack();
        if (condition != null) {
            logisticsTrack = JSONUtil.toBean(condition, LogisticsTrack.class);
        }
        List<LogisticsTrack> list = this.logisticsTrackService.getByCondition(logisticsTrack);
        return ApiResult.ok(list);
    }

    /**
     * 根据主订单号修改主订单
     */
    @RequestMapping(value = "/api/updateByMainOrderNo")
    public ApiResult updateByMainOrderNo(@RequestBody String value) {
        OrderInfo orderInfo = JSONUtil.toBean(value, OrderInfo.class);
        this.orderInfoService.updateByMainOrderNo(orderInfo.getOrderNo(), orderInfo.setOrderNo(null));
        return ApiResult.ok();
    }


    /**
     * 根据港口名称获取编码
     */
    @RequestMapping(value = "/api/getPortCodeByName")
    public ApiResult getPortCodeByName(@RequestBody String name) {
        PortInfo portInfo = new PortInfo().setName(name);
        List<PortInfo> portInfos = this.portInfoService.findPortInfoByCondition(portInfo);
        return ApiResult.ok(portInfos.size() > 0 ? portInfos.get(0).getIdCode() : null);
    }

    /**
     * 根据编码获取港口名称
     */
    @RequestMapping(value = "/api/getPortNameByCode")
    public ApiResult getPortNameByCode(@RequestBody String code) {
        PortInfo portInfo = new PortInfo().setIdCode(code);
        List<PortInfo> portInfos = this.portInfoService.findPortInfoByCondition(portInfo);
        return ApiResult.ok(portInfos.size() > 0 ? portInfos.get(0).getName() : null);
    }

    /**
     * 根据车辆主键查询车辆信息
     */
    @RequestMapping(value = "/api/getVehicleInfoById")
    public ApiResult getVehicleInfoById(@RequestParam("vehicleId") Long vehicleId) {
        VehicleInfo vehicleInfo = this.vehicleInfoService.getById(vehicleId);
        return ApiResult.ok(vehicleInfo);
    }


    @ApiOperation("根据主订单号查询法人主体信息")
    @RequestMapping(value = "/api/getLegalEntityInfoByOrderNo")
    public ApiResult getLegalEntityInfoByOrderNo(@RequestParam("mainOrderNo") String mainOrderNo) {
        List<OrderInfo> orderNos = this.orderInfoService.getByOrderNos(Collections.singletonList(mainOrderNo));
        if (orderNos.size() == 0) {
            log.warn("根据主订单查询法人主体信息失败 mainOrderNo={}", mainOrderNo);
            return ApiResult.error("根据主订单查询法人主体信息失败");
        }
        OrderInfo orderInfo = orderNos.get(0);
        ApiResult result = this.oauthClient.getLegalEntityByLegalId(orderInfo.getLegalEntityId());
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("根据接单法人id查询法人主体信息失败 legalEntityId={}", orderInfo.getLegalEntityId());
            return ApiResult.error("根据主订单查询法人主体信息失败");
        }

        return ApiResult.ok(result.getData());
    }


    @ApiOperation("批量保存/修改商品信息")
    @RequestMapping(value = "/api/saveOrUpdateGoodsBatch")
    public ApiResult saveOrUpdateGoodsBatch(@RequestBody List<AddGoodsForm> goodsForms) {
        List<Goods> goodsList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (AddGoodsForm goodsForm : goodsForms) {
            Goods goods = ConvertUtil.convert(goodsForm, Goods.class);
            goods.setCreateTime(goods.getId() == null ? now : null);
            goodsList.add(goods);
        }
        //批量保存货物信息
        if (goodsService.saveOrUpdateBatch(goodsList)) {
            return ApiResult.ok();
        } else {
            return ApiResult.error("批量保存/修改商品信息失败");
        }
    }

    @ApiOperation("批量保存/修改订单地址信息")
    @RequestMapping(value = "/api/saveOrUpdateOrderAddressBatch")
    public ApiResult saveOrUpdateOrderAddressBatch(@RequestBody List<AddOrderAddressForm> forms) {
        List<OrderAddress> addresses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (AddOrderAddressForm form : forms) {
            OrderAddress orderAddress = ConvertUtil.convert(form, OrderAddress.class);
            orderAddress.setCreateTime(orderAddress.getId() == null ? now : null);
            addresses.add(orderAddress);
        }
        //批量保存/修改订单地址信息
        if (this.orderAddressService.saveOrUpdateBatch(addresses)) {
            return ApiResult.ok();
        } else {
            return ApiResult.error("批量保存/修改订单地址信息失败");
        }
    }


    @ApiOperation("根据业务id集合查询订单地址")
    @RequestMapping(value = "/api/getOrderAddressByBusIds")
    public ApiResult<List<OrderAddress>> getOrderAddressByBusIds(@RequestParam("orderId") List<Long> orderId,
                                                                 @RequestParam("businessType") Integer businessType) {
        //查询订单地址信息
        List<OrderAddress> orderAddresses = this.orderAddressService.getOrderAddressByBusIds(orderId, businessType);
        return ApiResult.ok(orderAddresses);
    }

    @ApiOperation("根据时间区间查询订单id")
    @RequestMapping(value = "/api/getOrderAddressOrderIdByTimeInterval")
    public ApiResult<Set<Long>> getOrderAddressOrderIdByTimeInterval(@RequestParam("timeInterval") List<String> timeInterval,
                                                                     @RequestParam("type") Integer type,
                                                                     @RequestParam("businessType") Integer businessType) {
        Set<Long> businessIds = this.orderAddressService.getOrderAddressOrderIdByTimeInterval(new OrderAddress().setType(type).setBusinessType(businessType), timeInterval);
        return ApiResult.ok(businessIds);
    }


    @ApiOperation("根据订单id集合查询商品信息")
    @RequestMapping(value = "/api/getGoodsByBusIds")
    public ApiResult getGoodsByBusIds(@RequestParam("orderId") List<Long> orderId,
                                      @RequestParam("businessType") Integer businessType) {
        //查询商品信息
        List<Goods> goods = this.goodsService.getGoodsByBusIds(orderId, businessType);
        return ApiResult.ok(goods);
    }

    /**
     * 获取所有的车型尺寸
     *
     * @return
     */
    @RequestMapping(value = "/api/findVehicleSize")
    ApiResult<List<VehicleSizeInfoVO>> findVehicleSize() {
        List<VehicleSizeInfoVO> vehicleSizeInfoVOS = vehicleInfoService.findVehicleSize();
        return ApiResult.ok(vehicleSizeInfoVOS);
    }

    /**
     * 是否是虚拟仓
     *
     * @param warehouseInfoId
     * @return
     */
    @RequestMapping(value = "/api/isVirtualWarehouse")
    ApiResult<Boolean> isVirtualWarehouse(@RequestParam("warehouseInfoId") Long warehouseInfoId) {
        WarehouseInfo warehouseInfo = warehouseInfoService.getById(warehouseInfoId);
        if (warehouseInfo != null && warehouseInfo.getIsVirtual() != null) {
            return ApiResult.ok(warehouseInfo.getIsVirtual());
        }
        return ApiResult.ok(false);
    }

    /**
     * 根据供应商id集合查询供应商信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getSupplierInfoByIds")
    ApiResult<Collection<SupplierInfo>> getSupplierInfoByIds(@RequestParam("supplierIds") List<Long> supplierIds) {
        Collection<SupplierInfo> supplierInfos = supplierInfoService.listByIds(supplierIds);
        return ApiResult.ok(supplierInfos);
    }


    /**
     * 根据供应商名称查询供应商信息
     */
    @RequestMapping(value = "/api/getSupplierInfoByName")
    ApiResult<SupplierInfo> getSupplierInfoByName(@RequestParam("name") String name) {
        SupplierInfo supplierInfo = this.supplierInfoService.getByName(name);
        return ApiResult.ok(supplierInfo);
    }

    /**
     * 根据客户名称查询客户信息
     */
    @RequestMapping(value = "/api/getCustomerInfoByName")
    public ApiResult getCustomerInfoByName(String name) {
        CustomerInfo customerInfo = this.customerInfoService.getByName(name);
        return ApiResult.ok(customerInfo);
    }

    /**
     * 获取柜车大小类型
     */
    @RequestMapping(value = "api/getVehicleSizeInfo")
    ApiResult getCabinetType() {
        List<VehicleSizeInfoVO> vehicleSizeInfoVOS = vehicleInfoService.findVehicleSize();
        List<com.jayud.common.entity.InitComboxVO> cabinetCars = new ArrayList<>();
        for (VehicleSizeInfoVO obj : vehicleSizeInfoVOS) {
            if (VehicleTypeEnum.CABINET_CAR.getCode().equals(obj.getVehicleType())) {
                com.jayud.common.entity.InitComboxVO initComboxVO = new com.jayud.common.entity.InitComboxVO();
                initComboxVO.setId(obj.getId());
                initComboxVO.setName(obj.getVehicleSize());
                cabinetCars.add(initComboxVO);
            }
        }
        return ApiResult.ok(cabinetCars);
    }

    /**
     * 通关前审核/中港通关前复核页面详情
     *
     * @return
     */
    @RequestMapping(value = "/api/initGoCustomsAudit")
    public ApiResult initGoCustomsAudit(@RequestParam("mainOrderNo") String mainOrderNo) {
        InitGoCustomsAuditForm form = new InitGoCustomsAuditForm();
        form.setOrderNo(mainOrderNo);
        return ApiResult.ok(this.orderInfoService.initGoCustomsAudit(form));
    }

    /**
     * 是否费用提交审核(应收+应付)
     */
    @RequestMapping(value = "/api/isCostSubmitted")
    public ApiResult isCostSubmitted(@RequestParam("mainOrderNos") List<String> mainOrderNos
            , @RequestParam("orderNos") List<String> orderNos) {


        return ApiResult.ok();
    }


    /**
     * 批量修改费用状态
     *
     * @param costIds 费用主键
     * @param isBill
     * @param status  费用状态
     * @param type    类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/batchUpdateCostStatus")
    public ApiResult batchUpdateCostStatus(@RequestParam("costIds") List<Long> costIds,
                                           @RequestParam("isBill") String isBill,
                                           @RequestParam("status") Integer status,
                                           @RequestParam("type") Integer type) {
        switch (type) {
            case 0: //应收
                List<OrderReceivableCost> orderReceivableCosts = new ArrayList<>();
                for (Long costId : costIds) {
                    OrderReceivableCost orderReceivableCost = new OrderReceivableCost()
                            .setId(costId).setIsBill(isBill).setStatus(status);
                    orderReceivableCosts.add(orderReceivableCost);
                }
                return ApiResult.ok(this.orderReceivableCostService.updateBatchById(orderReceivableCosts));
            case 1: //应付
                List<OrderPaymentCost> orderPaymentCosts = new ArrayList<>();
                for (Long costId : costIds) {
                    OrderPaymentCost orderPaymentCost = new OrderPaymentCost()
                            .setId(costId).setIsBill(isBill).setStatus(status);
                    orderPaymentCosts.add(orderPaymentCost);
                }
                return ApiResult.ok(this.orderPaymentCostService.updateBatchById(orderPaymentCosts));
            default:
                return ApiResult.ok();
        }

    }

    /**
     * 根据费用主键集合批量查询费用币种信息
     *
     * @param costIds 费用主键
     * @param type    类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/getCostCurrencyInfo")
    public ApiResult getCostCurrencyInfo(@RequestParam("costIds") List<Long> costIds,
                                         @RequestParam("type") Integer type) {

        Set<String> currencyCodes = new HashSet<>();
        switch (type) {
            case 0: //应收
                Collection<OrderReceivableCost> orderReceivableCosts = this.orderReceivableCostService.listByIds(costIds);
                for (OrderReceivableCost orderReceivableCost : orderReceivableCosts) {
                    currencyCodes.add(orderReceivableCost.getCurrencyCode());
                }
                break;
            case 1: //应付
                Collection<OrderPaymentCost> orderPaymentCosts = this.orderPaymentCostService.listByIds(costIds);
                for (OrderPaymentCost orderPaymentCost : orderPaymentCosts) {
                    currencyCodes.add(orderPaymentCost.getCurrencyCode());
                }
                break;
        }
        return ApiResult.ok(this.currencyInfoService.getByCodes(currencyCodes));

    }

    /**
     * 根据费用主键集合批量查询费用信息
     *
     * @param costIds 费用主键
     * @param type    类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/getCostInfo")
    public ApiResult getCostInfo(@RequestParam("costIds") List<Long> costIds,
                                 @RequestParam("type") Integer type) {
        switch (type) {
            case 0: //应收
                Collection<OrderReceivableCost> orderReceivableCosts = this.orderReceivableCostService.listByIds(costIds);
                return ApiResult.ok(orderReceivableCosts);
            case 1: //应付
                Collection<OrderPaymentCost> orderPaymentCosts = this.orderPaymentCostService.listByIds(costIds);
                return ApiResult.ok(orderPaymentCosts);
        }
        return ApiResult.error("找不到对应费用");

    }

    /**
     * 根据订单号统计费用金额
     *
     * @param type 类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/statisticalCostByOrderNos")
    public ApiResult<Map<String, Map<String, BigDecimal>>> statisticalCostByOrderNos(@RequestParam("orderNos") List<String> orderNos,
                                                                                     @RequestParam("isMain") Boolean isMain,
                                                                                     @RequestParam("legalId") Long legalId,
                                                                                     @RequestParam("customerCode") String customerCode,
                                                                                     @RequestParam("type") Integer type) {
        QueryWrapper codition = new QueryWrapper<>();
        if (isMain) {
            codition.in("main_order_no", orderNos);
        } else {
            codition.in("order_no", orderNos);
        }
        codition.notIn("is_bill", "2", "save_confirm");
        codition.eq("is_sum_to_main", isMain);
        codition.eq("status", 3);
        codition.eq("legal_id", legalId);
        codition.eq("customer_code", customerCode);
        switch (type) {
            case 0: //应收
                List<OrderReceivableCost> orderReceivableCosts = this.orderReceivableCostService.getBaseMapper().selectList(codition);
                return ApiResult.ok(this.orderReceivableCostService.statisticalReCostByOrderNos(orderReceivableCosts, isMain));
            case 1: //应付
                List<OrderPaymentCost> orderPaymentCosts = this.orderPaymentCostService.getBaseMapper().selectList(codition);
                return ApiResult.ok(this.orderPaymentCostService.statisticalPayCostByOrderNos(orderPaymentCosts, isMain));
        }
        return ApiResult.error("找不到对应费用");

    }

    /**
     * 根据订单号查询未出账单费用
     *
     * @param type 类型(0:应收,1:应付)
     * @return
     */
    @RequestMapping(value = "/api/getNoBillCost")
    public ApiResult getNoBillCost(@RequestParam("orderNos") List<String> orderNos,
                                   @RequestParam("isMain") Boolean isMain,
                                   @RequestParam("legalId") Long legalId,
                                   @RequestParam("customerCode") String customerCode,
                                   @RequestParam("type") Integer type) {
        QueryWrapper codition = new QueryWrapper<>();
        if (isMain) {
            codition.in("main_order_no", orderNos);
        } else {
            codition.in("order_no", orderNos);
        }
        codition.notIn("is_bill", "2", "save_confirm");
        codition.eq("is_sum_to_main", isMain);
        codition.eq("status", 3);
        codition.eq("legal_id", legalId);
        codition.eq("customer_code", customerCode);
        switch (type) {
            case 0: //应收
                List<OrderReceivableCost> orderReceivableCosts = this.orderReceivableCostService.getBaseMapper().selectList(codition);
                return ApiResult.ok(orderReceivableCosts);
            case 1: //应付
                List<OrderPaymentCost> orderPaymentCosts = this.orderPaymentCostService.getBaseMapper().selectList(codition);
                return ApiResult.ok(orderPaymentCosts);
        }
        return ApiResult.error("找不到对应费用");

    }

    /**
     * 获取附件集合
     */
    @RequestMapping(value = "/api/getAttachments")
    ApiResult getAttachments(Long orderId) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        List<FileView> attachments = this.logisticsTrackService.getAttachments(orderId, BusinessTypeEnum.HY.getCode(), prePath);
        return ApiResult.ok(attachments);
    }

    /**
     * 获取附件集合
     */
    @RequestMapping(value = "/api/getTrailerAttachments")
    ApiResult getTrailerAttachments(Long orderId) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        List<FileView> attachments = this.logisticsTrackService.getAttachments(orderId, BusinessTypeEnum.TC.getCode(), prePath);
        return ApiResult.ok(attachments);
    }

    /**
     * 根据字典类型查询字典
     */
    @RequestMapping(value = "/api/getDictByDictTypeCode")
    public ApiResult getDictByDictTypeCode(@RequestParam("dictTypeCode") String dictTypeCode) {
        List<Dict> list = this.dictService.getByDictTypeCode(dictTypeCode);
        return ApiResult.ok(list);
    }

    /**
     * 根据字典类型下拉选项字典
     */
    @RequestMapping(value = "/api/initDictNameByDictTypeCode")
    public ApiResult<List<com.jayud.common.entity.InitComboxVO>> initDictNameByDictTypeCode(@RequestParam("dictTypeCode") String dictTypeCode) {
        List<Dict> list = this.dictService.getByDictTypeCode(dictTypeCode);
        return ApiResult.ok(list);
    }

    /**
     * 根据字典类型下拉选项字典
     */
    @RequestMapping(value = "/api/initDictByDictTypeCode")
    public ApiResult<List<InitComboxStrVO>> initDictByDictTypeCode(@RequestParam("dictTypeCode") String dictTypeCode) {
        List<Dict> list = this.dictService.getByDictTypeCode(dictTypeCode);
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (Dict dict : list) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(dict.getValue());
            initComboxStrVO.setCode(dict.getCode());
            initComboxStrVOS.add(initComboxStrVO);
        }
        return ApiResult.ok(initComboxStrVOS);
    }

    /**
     * 根据客户code集合查询客户信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getCustomerByUnitCode")
    ApiResult getCustomerByUnitCode(@RequestBody List<String> unitCodes) {
        List<CustomerInfo> customerInfos = new ArrayList<>();
        for (String unitCode : unitCodes) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id_code", unitCode);
            CustomerInfo one = customerInfoService.getOne(queryWrapper);
            customerInfos.add(one);
        }
        return ApiResult.ok(customerInfos);
    }

    /**
     * 查询所有客户名称
     *
     * @return
     */
    @RequestMapping(value = "/api/getCustomerName")
    ApiResult<Map<String, String>> getCustomerName() {
        List<CustomerInfo> list = this.customerInfoService.list();
        Map<String, String> map = this.customerInfoService.getCustomerName();
        return ApiResult.ok(map);
    }

    /**
     * 获取订单号
     *
     * @return
     */
    @RequestMapping(value = "/api/getOrderNo")
    ApiResult getOrderNo(@RequestParam("preOrder") String preOrder, @RequestParam("classCode") String classCode) {
        String orderNo = orderTypeNumberService.getOrderNo(preOrder, classCode);
        return ApiResult.ok(orderNo);
    }


    /**
     * 订单地址(保存提货/送货地址)
     *
     * @return
     */
    @RequestMapping(value = "/api/addDeliveryAddress")
    public ApiResult addDeliveryAddress(@RequestBody List<OrderDeliveryAddress> deliveryAddressList) {
        this.orderAddressService.addDeliveryAddress(deliveryAddressList);
        return ApiResult.ok();
    }

    /**
     * 批量新增/修改订单流程
     *
     * @return
     */
    @RequestMapping(value = "/api/batchAddOrUpdateProcess")
    public ApiResult batchAddOrUpdateProcess(@RequestBody List<OrderFlowSheet> orderFlowSheets) {
        this.orderFlowSheetService.saveOrUpdateBatch(orderFlowSheets);
        return ApiResult.ok();
    }

    /**
     * 获取订单节点
     *
     * @return
     */
    @RequestMapping(value = "/api/getOrderProcessNode")
    public ApiResult<String> getOrderProcessNode(@RequestParam("mainOrderNo") String mainOrderNo,
                                                 @RequestParam("orderNo") String orderNo,
                                                 @RequestParam("currentNodeStatus") String currentNodeStatus) {
        List<OrderFlowSheet> orderFlowSheets = orderFlowSheetService.getByCondition(new OrderFlowSheet().setMainOrderNo(mainOrderNo)
                .setOrderNo(orderNo).setFStatus(currentNodeStatus));
        return ApiResult.ok(orderFlowSheets.get(0).getStatus());
    }

    /**
     * 获取提货/送货地址信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getDeliveryAddress")
    public ApiResult<List<OrderDeliveryAddress>> getDeliveryAddress(@RequestParam("orderId") List<Long> orderId,
                                                                    @RequestParam("businessType") Integer businessType) {
        List<OrderDeliveryAddress> list = this.orderAddressService.getDeliveryAddress(orderId, businessType);
        return ApiResult.ok(list);
    }

    /**
     * 根据车辆ids查询车辆信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getVehicleInfoByIds")
    public ApiResult<List<VehicleInfo>> getVehicleInfoByIds(@RequestParam("orderIds") List<Long> vehicleIds) {
        if (CollectionUtils.isEmpty(vehicleIds)) {
            return ApiResult.ok();
        }
        Collection<VehicleInfo> vehicleInfos = this.vehicleInfoService.listByIds(vehicleIds);
        return ApiResult.ok(new ArrayList<>(vehicleInfos));
    }


    /**
     * 根据司机id查询 司机信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getDriverInfoByIdOne")
    public ApiResult getDriverInfoByIdOne(@RequestParam("driverName") String driverName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", driverName);
        DriverInfo driverInfo = this.driverInfoService.getOne(queryWrapper);
        return ApiResult.ok(driverInfo);
    }

    /**
     * 根据司机ids查询司机信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getDriverInfoByIds")
    public ApiResult<List<DriverInfo>> getDriverInfoByIds(@RequestParam("driverIds") List<Long> driverIds) {
        Collection<DriverInfo> driverInfos = this.driverInfoService.listByIds(driverIds);
        return ApiResult.ok(new ArrayList<>(driverInfos));
    }

    /**
     * 根据中转仓库ids查询中转仓库信息
     *
     * @return
     */
    @RequestMapping(value = "/api/getWarehouseInfoByIds")
    public ApiResult<List<WarehouseInfo>> getWarehouseInfoByIds(@RequestParam("warehouseIds") List<Long> warehouseIds) {
        Collection<WarehouseInfo> warehouseInfos = this.warehouseInfoService.listByIds(warehouseIds);
        return ApiResult.ok(new ArrayList<>(warehouseInfos));
    }


    @ApiOperation(value = "单个存储商品信息")
    @RequestMapping(value = "api/saveOrUpdateGood")
    ApiResult saveOrUpdateGood(@RequestBody AddGoodsForm goodsForm) {
        LocalDateTime now = LocalDateTime.now();
        Goods goods = ConvertUtil.convert(goodsForm, Goods.class);
        goods.setCreateTime(goods.getId() == null ? now : null);
        this.goodsService.saveOrUpdate(goods);
        return ApiResult.ok(goods.getId());
    }

    @ApiOperation(value = "根据id获取商品信息")
    @RequestMapping(value = "api/getGoodById")
    ApiResult getGoodById(@RequestParam("id") Long id) {
        Goods goods = this.goodsService.getById(id);
        return ApiResult.ok(goods);
    }


    @ApiModelProperty(value = "获取菜单待处理数")
    @RequestMapping(value = "/api/getMenuPendingNum")
    public ApiResult getMenuPendingNum(@RequestBody List<Map<String, Object>> menusList) {
        Map<String, String> tmp = new HashMap<>();
        tmp.put("外部报关放行", "outPortPass");
        tmp.put("通关前审核", "portPassCheck");
        tmp.put("费用审核", "feeCheck");
        tmp.put("应收对账单审核", "mainReceiverCheck");
        tmp.put("应付对账单审核", "mainPayCheck");

        List<Map<String, Object>> result = new ArrayList<>();

        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();
        Map<String, Integer> reBillNumMap = this.financeClient.getPendingBillStatusNum(null, legalIds, 0, true, SubOrderSignEnum.MAIN.getSignOne()).getData();
        Map<String, Integer> payBillNumMap = this.financeClient.getPendingBillStatusNum(null, legalIds, 1, true, SubOrderSignEnum.MAIN.getSignOne()).getData();


        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), UserTypeEnum.EMPLOYEE_TYPE.getCode()).getData();

        for (Map<String, Object> menus : menusList) {

            Map<String, Object> map = new HashMap<>();
            Object title = menus.get("title");
            String cmd = tmp.get(title);
            Integer num = 0;
            if (cmd != null) {
                switch (cmd) {
                    case "outPortPass":
                        num = this.orderInfoService.pendingExternalCustomsDeclarationNum(dataControl);
                        break;
                    case "portPassCheck":
                        num = this.orderInfoService.filterGoCustomsAudit(null, dataControl, null).size();
                        break;
                    case "feeCheck":
                        num = this.costCommonService.auditPendingExpenses(SubOrderSignEnum.MAIN.getSignOne(), dataControl, null, null);
                        break;
                    case "mainReceiverCheck":
                        num = reBillNumMap.get("B_1");
                        break;
                    case "mainPayCheck":
                        num = payBillNumMap.get("B_1");
                        break;
                }

            }
            map.put("menusName", title);
            map.put("num", num);
            result.add(map);
        }
        return ApiResult.ok(result);
    }

    @ApiOperation(value = "查询联系人信息")
    @RequestMapping(value = "/getContactInfoByPhone")
    public CommonResult<List<Map<String, Object>>> getContactInfoByPhone(@RequestParam("businessType") Integer businessType) {
        List<OrderAddress> deliveryAddresses = this.orderAddressService.getLastContactInfoByBusinessType(businessType);
        List<Map<String, Object>> list = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(deliveryAddresses)) {
            //去重
            deliveryAddresses = deliveryAddresses.stream().collect(Collectors
                    .collectingAndThen(Collectors.toCollection(
                            () -> new TreeSet<>(Comparator.comparing(OrderAddress::getPhone))), ArrayList::new));
            for (OrderAddress deliveryAddress : deliveryAddresses) {
                Map<String, Object> response = new HashMap<>();
                response.put(BeanUtils.convertToFieldName(DeliveryAddress::getContacts), deliveryAddress.getContacts());
                response.put("value", deliveryAddress.getPhone());
                response.put(BeanUtils.convertToFieldName(DeliveryAddress::getAddress), deliveryAddress.getAddress());
                list.add(response);
            }
            return CommonResult.success(list);
        }
        return CommonResult.success(list);
    }


    @ApiOperation(value = "根据司机id查询司机信息")
    @RequestMapping(value = "/api/getDriverById")
    public ApiResult<DriverInfo> getDriverById(@RequestParam("driverId") Long driverId) {
        return ApiResult.ok(this.driverInfoService.getById(driverId));
    }


    @ApiOperation(value = "主订单驳回标识操作")
    @RequestMapping(value = "/api/doMainOrderRejectionSignOpt")
    public ApiResult<Boolean> doMainOrderRejectionSignOpt(@RequestParam("mainOrderNo") String mainOrderNo,
                                                          @RequestParam("rejectionDesc") String rejectionDesc) {
        List<OrderInfo> mainOrderInfos = orderInfoService.getByOrderNos(Collections.singletonList(mainOrderNo));
        OrderInfo orderInfo = mainOrderInfos.get(0);
        return ApiResult.ok(orderInfoService.updateById(new OrderInfo().setId(orderInfo.getId())
                .setIsRejected(true).setRejectComment(rejectionDesc)));
    }


    @ApiOperation(value = "获取过滤订单状态数量")
    @RequestMapping(value = "/api/getFilterOrderStatus")
    public ApiResult<Integer> getFilterOrderStatus(@RequestParam("mainOrderNos") List<String> mainOrderNos,
                                                   @RequestParam("status") Integer status) {
        List<OrderInfo> mainOrderInfos = orderInfoService.getOrderByStatus(mainOrderNos, status);

        Map<String, String> map = mainOrderInfos.stream().collect(Collectors.toMap(OrderInfo::getOrderNo, OrderInfo::getOrderNo));

        int count = 0;
        for (String mainOrderNo : mainOrderNos) {
            if (map.get(mainOrderNo) != null) {
                ++count;
            }
        }

        return ApiResult.ok(count);
    }

    @ApiOperation(value = "根据子订单id查询所有子订单物流轨迹")
    @RequestMapping(value = "/api/getLogisticsTrackBySubId")
    public ApiResult<Integer> getLogisticsTrackByType(@RequestParam("subOrderIds") List<Long> subOrderIds,
                                                      @RequestParam("type") Integer type) {
        List<LogisticsTrack> logisticsTracks = this.logisticsTrackService.getLogisticsTrackByType(subOrderIds, type);
        return ApiResult.ok(logisticsTracks);
    }


    @ApiOperation(value = "获取订单id")
    @RequestMapping(value = "/api/getMainOrderByOrderNo")
    ApiResult<Long> getMainOrderByOrderNo(String mainOrderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", mainOrderNo);
        OrderInfo one = orderInfoService.getOne(queryWrapper);
        return ApiResult.ok(one.getId());
    }

    @ApiOperation("根据业务id集合查询订单地址")
    @RequestMapping(value = "/api/getOrderAddressByBusOrders")
    public ApiResult<List<OrderAddress>> getOrderAddressByBusOrders(@RequestParam("orderNo") List<String> orderNo,
                                                                    @RequestParam("businessType") Integer businessType) {
        //查询订单地址信息
        List<OrderAddress> orderAddresses = this.orderAddressService.getOrderAddressByBusOrders(orderNo, businessType);
        return ApiResult.ok(orderAddresses);
    }


    @ApiOperation("根据订单id集合查询商品信息")
    @RequestMapping(value = "/api/getGoodsByBusOrders")
    public ApiResult<List<Goods>> getGoodsByBusOrders(@RequestParam("orderNo") List<String> orderNo,
                                                      @RequestParam("businessType") Integer businessType) {
        //查询商品信息
        List<Goods> goods = this.goodsService.getGoodsByBusOrders(orderNo, businessType);
        return ApiResult.ok(goods);
    }


    @ApiOperation("根据订单号删除订单地址")
    @RequestMapping(value = "/api/deleteOrderAddressByBusOrders")
    public ApiResult deleteOrderAddressByBusOrders(@RequestParam("orderNo") List<String> orderNo,
                                                   @RequestParam("businessType") Integer businessType) {
        //查询订单地址信息
        this.orderAddressService.deleteOrderAddressByBusOrders(orderNo, businessType);
        return ApiResult.ok();
    }


    @ApiOperation("根据订单号删除商品信息")
    @RequestMapping(value = "/api/deleteGoodsByBusOrders")
    public ApiResult deleteGoodsByBusOrders(@RequestParam("orderNo") List<String> orderNo,
                                            @RequestParam("businessType") Integer businessType) {
        //查询商品信息
        this.goodsService.deleteGoodsByBusOrders(orderNo, businessType);
        return ApiResult.ok();
    }

    /**
     * 根据字典名称获取字典代码
     *
     * @param supervisionMode
     * @return
     */
    @RequestMapping(value = "/api/getDictCodeByDictTypeName")
    public ApiResult<String> getDictCodeByDictTypeName(@RequestParam("supervisionMode") String supervisionMode) {
        List<Dict> supervisionMode1 = dictService.getByDictTypeCode("supervisionMode");
        for (Dict dict : supervisionMode1) {
            if (dict.getValue().equals(supervisionMode)) {
                return ApiResult.ok(dict.getCode());
            }
        }
        return ApiResult.error("数据匹配失败");
    }


    /**
     * 是否录用费用
     *
     * @return
     */
    @RequestMapping(value = "/api/isCost")
    public ApiResult<Map<String, Object>> isCost(@RequestBody List<String> orderNos,
                                                 @RequestParam("subType") String subType) {

        List<OrderPaymentCost> payments = this.orderPaymentCostService.getByType(orderNos, subType);
        Map<String, Object> map = new HashMap<>();
        List<OrderReceivableCost> receivables = this.orderReceivableCostService.getByType(orderNos, subType);

        if (SubOrderSignEnum.MAIN.getSignOne().equals(subType)) {
            Map<String, Long> tmp = payments.stream().collect(Collectors.groupingBy(OrderPaymentCost::getMainOrderNo, Collectors.counting()));
            map.putAll(tmp);
            tmp = receivables.stream().collect(Collectors.groupingBy(OrderReceivableCost::getMainOrderNo, Collectors.counting()));
            map.putAll(tmp);
        } else {
            Map<String, Long> tmp = payments.stream().collect(Collectors.groupingBy(OrderPaymentCost::getOrderNo, Collectors.counting()));
            map.putAll(tmp);
            tmp = receivables.stream().collect(Collectors.groupingBy(OrderReceivableCost::getOrderNo, Collectors.counting()));
            map.putAll(tmp);
        }

        for (String orderNo : orderNos) {
            map.put(orderNo, map.get(orderNo) != null);
        }

        return ApiResult.ok(map);
    }

    /**
     * 获取入仓号
     *
     * @return
     */
    @RequestMapping(value = "/api/getWarehouseNumber")
    ApiResult getWarehouseNumber(@RequestParam("preOrder") String preOrder) {
        String warehouseNumber = orderTypeNumberService.getWarehouseNumber(preOrder);
        System.out.println("warehouseNumber============================" + warehouseNumber);
        return ApiResult.ok(warehouseNumber);
    }

    @ApiOperation(value = "查询客户id")
    @RequestMapping(value = "/api/getCustomerByCode")
    public ApiResult<Long> getCustomerByCode(@RequestParam("code") String code) {
        CustomerInfo byCode = customerInfoService.getByCode(code);
        return ApiResult.ok(byCode.getId());
    }

    @ApiOperation(value = "查询客户名称")
    @RequestMapping(value = "/api/getCustomerNameByCode")
    public ApiResult<String> getCustomerNameByCode(@RequestParam("code") String code) {
        CustomerInfo byCode = customerInfoService.getByCode(code);
        return ApiResult.ok(byCode.getName());
    }

    @ApiOperation(value = "查询客户名称")
    @RequestMapping(value = "/api/getCustomerNameById")
    public ApiResult<String> getCustomerNameById(@RequestParam("id") Long id) {
        CustomerInfo byCode = customerInfoService.getById(id);
        return ApiResult.ok(byCode.getName());
    }

    /**
     * 根据审核表唯一标识查询审核描述(对账单)
     *
     * @param extUniqueFlags
     * @return
     */
    @RequestMapping(value = "/api/getByExtUniqueFlag")
    public ApiResult<Map<String, Object>> getByExtUniqueFlag(@RequestBody List<String> extUniqueFlags) {
        List<Map<String, Object>> list = this.auditInfoService.getByExtUniqueFlag(extUniqueFlags);
        Map<Object, Object> map = list.stream().collect(Collectors.toMap(e -> e.get("extUniqueFlag"), e -> e.get("auditComment")));
        return ApiResult.ok(map);
    }


    /**
     * 应收/应付费用状态
     *
     * @return
     */
    @RequestMapping(value = "/api/getCostStatus")
    public ApiResult<Map<String, Object>> getCostStatus(@RequestParam(value = "mainOrderNos", required = false) List<String> mainOrderNos,
                                                        @RequestParam(value = "orderNos", required = false) List<String> orderNos) {
        Map<String, Object> costStatus = this.orderInfoService.getCostStatus(mainOrderNos, orderNos);
        return ApiResult.ok(costStatus);
    }

    /**
     * 查询待审核费用订单数量
     *
     * @return
     */
    @RequestMapping(value = "/api/auditPendingExpenses")
    public ApiResult<Integer> auditPendingExpenses(@RequestParam("subType") String subType,
                                                   @RequestParam("legalIds") List<Long> legalIds,
                                                   @RequestParam("orderNos") List<String> orderNos) {
        DataControl dataControl = new DataControl();
        dataControl.setCompanyIds(legalIds);
        Integer num = this.costCommonService.auditPendingExpenses(subType, dataControl, orderNos, null);
        return ApiResult.ok(num);
    }

    /**
     * 查询待审核费用订单数量
     *
     * @return
     */
    @RequestMapping(value = "/api/auditPendingExpensesNum")
    public ApiResult<Integer> auditPendingExpensesNum(@RequestParam("subType") String subType,
                                                      @RequestBody(required = false) DataControl dataControl,
                                                      @RequestParam("orderNos") List<String> orderNos) {
        Integer num = this.costCommonService.auditPendingExpenses(subType, dataControl, orderNos, null);
        return ApiResult.ok(num);
    }


    /**
     * 客户管理菜单待处理数量
     *
     * @param menusList
     * @return
     */
    @RequestMapping(value = "/api/getCustomerMenuPendingNum")
    public ApiResult<Map<String, String>> getCustomerMenuPendingNum(@RequestBody List<Map<String, Object>> menusList) {
        if (CollectionUtil.isEmpty(menusList)) {
            return ApiResult.ok();
        }
        Map<String, String> tmp = new HashMap<>();
        tmp.put("客服审核", "customer_service");
        tmp.put("财务审核", "Finance");
        tmp.put("总经办审核", "General_classics");

        List<Map<String, Object>> result = new ArrayList<>();

        ApiResult<List<Long>> legalEntityByLegalName = this.oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = legalEntityByLegalName.getData();

        for (Map<String, Object> menus : menusList) {

            Map<String, Object> map = new HashMap<>();
            Object title = menus.get("title");
            String status = tmp.get(title);
            Integer num = 0;
            if (status != null) {
                num = this.customerInfoService.getNumByStatus(status, legalIds);
            }
            map.put("menusName", title);
            map.put("num", num);
            result.add(map);
        }
        return ApiResult.ok(result);
    }


    /**
     * 供应商管理菜单待处理数量
     *
     * @param menusList
     * @return
     */
    @RequestMapping(value = "/api/getSupplierMenuPendingNum")
    public ApiResult<Map<String, String>> getSupplierMenuPendingNum(@RequestBody List<Map<String, Object>> menusList) {
        if (CollectionUtil.isEmpty(menusList)) {
            return ApiResult.ok();
        }
        Map<String, String> tmp = new HashMap<>();
        tmp.put("财务审核", "financialCheck");
        tmp.put("总经办审核", "managerCheck");
//        Map<String,String> supplierUser=new HashMap<>();
//        supplierUser.put("派车", "T_1");
//        supplierUser.put("提货", "T_4");
//        supplierUser.put("过磅", "T_5");
//        supplierUser.put("驳回", "T_3_1");
//        supplierUser.put("通关", "T_8");
//        supplierUser.put("派送", "T_13");
//        supplierUser.put("签收", "T_14");

        List<Map<String, Object>> result = new ArrayList<>();

//        ApiResult<List<Long>> legalEntityByLegalName = this.oauthClient.getLegalIdBySystemName(UserOperator.getToken());
//        List<Long> legalIds = legalEntityByLegalName.getData();
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), null).getData();
        //中港待处理节点
        Map<String, Integer> tmsPendingNum = null;
        if (UserTypeEnum.SUPPLIER_TYPE.getCode().equals(dataControl.getAccountType())) {
            tmsPendingNum = this.tmsClient.getNumByStatus("supplier", dataControl).getData();
        }

        for (Map<String, Object> menus : menusList) {
            Map<String, Object> map = new HashMap<>();
            Object title = menus.get("title");
            Integer num = 0;
            if (tmsPendingNum != null) {
                num = tmsPendingNum.get(title);
            } else {
                String status = tmp.get(title);
                if (status != null) {
                    num = this.supplierInfoService.getNumByStatus(status, dataControl.getCompanyIds());
                }
            }
            map.put("menusName", title);
            map.put("num", num);
            result.add(map);
        }
        return ApiResult.ok(result);
    }

    @ApiOperation(value = "查询在子订单录入费用的订单号")
    @RequestMapping(value = "/api/getPaymentCost")
    ApiResult<List<String>> getPaymentCost(@RequestParam("subType") String subType) {
        List<OrderPaymentCost> orderPaymentCosts = orderPaymentCostService.getBySubType(subType);
        List<String> list = new ArrayList<>();
        for (OrderPaymentCost orderPaymentCost : orderPaymentCosts) {
            list.add(orderPaymentCost.getOrderNo());
        }
        return ApiResult.ok(list);
    }

    @ApiOperation(value = "查询在子订单录入费用的订单号")
    @RequestMapping(value = "/api/getReceivableCost")
    ApiResult<List<String>> getReceivableCost(@RequestParam("subType") String subType) {
        List<OrderReceivableCost> orderReceivableCosts = orderReceivableCostService.getBySubType(subType);
        List<String> list = new ArrayList<>();
        for (OrderReceivableCost orderReceivableCost : orderReceivableCosts) {
            list.add(orderReceivableCost.getOrderNo());
        }
        return ApiResult.ok(list);
    }

    @ApiOperation(value = "获取主订单客户名称")
    @RequestMapping(value = "/api/getCustomerNameByOrderNo")
    ApiResult getCustomerNameByOrderNo(@RequestParam(value = "orderNo") String orderNo) {
        OrderInfo orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getOrderNo, orderNo));
        return ApiResult.ok(Objects.nonNull(orderInfo) ? orderInfo.getCustomerName() : "");
    }


    @ApiOperation(value = "根据费用类型查询费用名称")
    @PostMapping(value = "/getCostInfoByCostType")
    public ApiResult<List<InitComboxStrVO>> getCostInfoByCostType(@RequestBody Map<String, String> map) {
        String code = MapUtil.getStr(map, "code");
        String name = MapUtil.getStr(map, "name");
//        if (StringUtils.isEmpty(code) && StringUtils.isEmpty(name)) {
//            return CommonResult.error(ResultEnum.PARAM_ERROR);
//        }
        if (!StringUtils.isEmpty(code)) {
            return ApiResult.ok(this.costInfoService.getCostInfoByCostTypeCode(code));
        }
        if (!StringUtils.isEmpty(name)) {
            return ApiResult.ok(this.costInfoService.getCostInfoByCostTypeCode(name));
        }

        return ApiResult.error(ResultEnum.PARAM_ERROR.getMessage());
    }


    /**
     * 查询供应商费用
     */
    @PostMapping(value = "/getSupplierCost")
    public ApiResult<OrderPaymentCost> getSupplierCost(@RequestParam("supplierId") Long supplierId) {
        List<OrderPaymentCost> list = this.paymentCostService.getByCondition(new OrderPaymentCost().setSupplierId(supplierId));
        return ApiResult.ok(list);
    }

    /**
     * 根据子订单号集合查询供应商费用
     */
    @PostMapping(value = "/getSupplierPayCostByOrderNos")
    public ApiResult<Map<String, Map<String, BigDecimal>>> statisticalSupplierPayCostByOrderNos(@RequestParam("supplierId") Long supplierId,
                                                                                                @RequestParam("orderNos") List<String> subOrderNos) {
        List<OrderPaymentCost> list = this.paymentCostService.getSupplierPayCostByOrderNos(supplierId, subOrderNos,
                Integer.valueOf(OrderStatusEnum.COST_3.getCode()));
        Map<String, Map<String, BigDecimal>> map = this.paymentCostService.statisticalPayCostByOrderNos(list, false);
        return ApiResult.ok(map);
    }

    /**
     * 根据提货时间获取订单号
     *
     * @param takeTimeStr
     * @param code
     * @return
     */
    @PostMapping(value = "/api/getOrderNosByTakeTime")
    public ApiResult<Set<String>> getOrderNosByTakeTime(@RequestParam("takeTimeStr") String[] takeTimeStr, @RequestParam("code") Integer code) {
        Set<String> orderNos = orderAddressService.getOrderNosByTakeTime(takeTimeStr, code);
        return ApiResult.ok(orderNos);
    }


    @ApiOperation(value = "获取公司名称下拉列表")
    @RequestMapping(value = "/api/getCustomerInfo")
    ApiResult getCustomerInfo() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", 1);
        return ApiResult.ok(customerInfoService.list(queryWrapper));
    }

    @ApiOperation(value = "获取该订单所有应收费用")
    @RequestMapping(value = "/api/getOrderReceivableCostByMainOrderNo")
    ApiResult<List<OrderReceivableCostVO>> getOrderReceivableCostByMainOrderNo(@RequestParam("mainOrderNo") String mainOrderNo) {
        List<OrderReceivableCost> byMainOrderNo = receivableCostService.getOrderReceivableCostByMainOrderNo(mainOrderNo);
        List<OrderReceivableCostVO> orderReceivableCostVOS = new ArrayList<>();
        for (OrderReceivableCost orderReceivableCost : byMainOrderNo) {
            OrderReceivableCostVO convert = ConvertUtil.convert(orderReceivableCost, OrderReceivableCostVO.class);
            convert.setCostName(costInfoService.getCostNameByCostCode(convert.getCostCode()));
            orderReceivableCostVOS.add(convert);
        }
        return ApiResult.ok(orderReceivableCostVOS);
    }

    @ApiOperation(value = "获取该币种转换成美元的汇率")
    @RequestMapping(value = "/api/getExchangeRateByCurrency")
    ApiResult<BigDecimal> getExchangeRateByCurrency(@RequestParam("currencyCode") String currencyCode, @RequestParam("usd") String usd, @RequestParam("month") String month) {
        CurrencyInfoVO currencyInfo = currencyInfoService.getCurrencyInfoByCode(currencyCode, usd, month);
        if (currencyInfo != null) {
            return ApiResult.ok(currencyInfo.getExchangeRate());
        } else {
            CurrencyInfoVO currencyInfo1 = currencyInfoService.getCurrencyInfoByCode(usd, currencyCode, month);
            if (currencyInfo1 != null) {
                return ApiResult.ok(currencyInfo1.getExchangeRate());
            } else {
                return ApiResult.ok(new BigDecimal(0));
            }
        }

    }

    @ApiOperation(value = "根据主订单号集合获取订单利润")
    @RequestMapping(value = "/api/getOrderCostByMainOrderNos")
    ApiResult<List<SeaOrderProfitVO>> getOrderCostByMainOrderNos(@RequestBody List<String> mainOrderNos) {
        List<OrderReceivableCost> receivableCosts = receivableCostService.getOrderReceivableCostByMainOrderNos(mainOrderNos);
        List<OrderPaymentCost> paymentCosts = paymentCostService.getOrderPaymentCostByMainOrderNos(mainOrderNos);
        List<InputPaymentCostVO> inputPaymentCostVOS = ConvertUtil.convertList(paymentCosts, InputPaymentCostVO.class);
        List<InputReceivableCostVO> inputReceivableCostVOS = ConvertUtil.convertList(receivableCosts, InputReceivableCostVO.class);
        Map<String, List<InputPaymentCostVO>> map = new HashMap<>();
        Map<String, List<InputReceivableCostVO>> map1 = new HashMap<>();
        for (String mainOrderNo : mainOrderNos) {

            List<InputReceivableCostVO> orderReceivableCosts = new ArrayList<>();
            List<InputPaymentCostVO> orderPaymentCosts = new ArrayList<>();

            for (InputReceivableCostVO receivableCost : inputReceivableCostVOS) {
                if (receivableCost.getMainOrderNo().equals(mainOrderNo)) {
                    orderReceivableCosts.add(receivableCost);
                }
            }
            for (InputPaymentCostVO paymentCost : inputPaymentCostVOS) {
                if (paymentCost.getMainOrderNo().equals(mainOrderNo)) {
                    orderPaymentCosts.add(paymentCost);
                }
            }
            map.put(mainOrderNo, orderPaymentCosts);
            map1.put(mainOrderNo, orderReceivableCosts);
        }

        List<SeaOrderProfitVO> seaOrderProfitVOS = new ArrayList<>();
        for (String mainOrderNo : mainOrderNos) {
            InputCostVO inputCostVO = new InputCostVO();
            inputCostVO.setPaymentCostList(map.get(mainOrderNo));
            inputCostVO.setReceivableCostList(map1.get(mainOrderNo));
            orderInfoService.calculateCost(inputCostVO);

            SeaOrderProfitVO seaOrderProfitVO = new SeaOrderProfitVO();
            seaOrderProfitVO.setMainOrderNo(mainOrderNo);
            seaOrderProfitVO.setProfit(inputCostVO.getProfit());
            seaOrderProfitVOS.add(seaOrderProfitVO);
        }

        return ApiResult.ok(seaOrderProfitVOS);
    }

    /**
     * 批量保存拖车地址和商品信息
     *
     * @param orderAddressForms
     * @return
     */
    @RequestMapping(value = "/api/saveOrUpdateOrderAddressAndGoodsBatch")
    ApiResult saveOrUpdateOrderAddressAndGoodsBatch(@RequestBody List<AddTrailerOrderAddressForm> orderAddressForms) {
        this.orderAddressService.saveOrUpdateOrderAddressAndGoodsBatch(orderAddressForms);
        return ApiResult.ok();
    }

    @ApiOperation(value = "下拉业务类型")
    @PostMapping(value = "/initBizService")
    public CommonResult<List<InitComboxStrVO>> initBizService() {
        //业务类型
        List<ProductBiz> productBizs = productBizService.findProductBiz();
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (ProductBiz productBiz : productBizs) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(productBiz.getIdCode());
            comboxStrVO.setName(productBiz.getName());
            comboxStrVOS.add(comboxStrVO);
        }

        return CommonResult.success(comboxStrVOS);
    }

    @ApiOperation(value = "获取启用费用名称")
    @PostMapping(value = "/getCostInfos")
    public CommonResult<List<CostInfo>> getCostInfos() {
        //费用名称
        List<CostInfo> costInfos = costInfoService.list(new QueryWrapper<>(new CostInfo().setStatus(StatusEnum.ENABLE.getCode())));
        return CommonResult.success(costInfos);
    }

    @ApiOperation(value = "获取启用费用类型")
    @PostMapping(value = "/getCostTypes")
    public CommonResult<List<CostType>> getCostTypes() {
        List<CostType> costTypes = costTypeService.list(new QueryWrapper<>(new CostType().setStatus(StatusEnum.ENABLE.getCode())));
        return CommonResult.success(costTypes);
    }

    /**
     * 根据费用id查询所有费用并且统计
     *
     * @param reCostIds
     * @param payCostIds
     * @return
     */
    @ApiOperation(value = "根据费用id查询所有费用并且统计")
    @PostMapping(value = "/getCostDetailByCostIds")
    public ApiResult<InputCostVO> getCostDetailByCostIds(@RequestParam(value = "reCostIds") List<Long> reCostIds,
                                                         @RequestParam(value = "payCostIds") List<Long> payCostIds) {

        return ApiResult.ok(this.orderInfoService.getCostDetailByCostIds(reCostIds, payCostIds));
    }

    /**
     *
     */
    @RequestMapping(value = "/api/getPortInfoALL")
    public ApiResult<Map<String, String>> getPortInfoALL() {
        return ApiResult.ok(this.portInfoService.list().stream().collect(Collectors.toMap(e -> e.getIdCode(), e -> e.getName())));
    }

    @ApiOperation(value = "查询字典")
    @PostMapping(value = "/api/findDict")
    public ApiResult<List<Dict>> findDictType(@RequestParam("dictTypeCode") String dictTypeCode) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dict::getDictTypeCode, dictTypeCode);
        List<Dict> dictList = this.dictService.list(queryWrapper);
        return ApiResult.ok(dictList);
    }

    /**
     * 创建消息推送任务
     */
    @ApiOperation(value = "创建消息推送任务")
    @RequestMapping(value = "/api/createPushTask")
    public ApiResult createPushTask(@RequestBody String msg) {
        JSONObject jsonObject = new JSONObject(msg);
        String triggerStatus = jsonObject.getStr("triggerStatus");
        Map<String, Object> sqlParam = jsonObject.get("sqlParam", Map.class);
        LocalDateTime now = DateUtils.str2LocalDateTime(jsonObject.getStr("now"), DateUtils.DATE_TIME_PATTERN);
        Map<String, Object> otherParam = new HashMap<>();
        String cmd = jsonObject.getStr("cmd");
        MsgPushInstructionEnum cmdEnum = MsgPushInstructionEnum.getEnum(cmd);
        if (cmdEnum != null) {
            switch (cmdEnum) {
                case CMD1:
                    List<Long> associatedUserId = new ArrayList<>();
                    otherParam.put("associatedUserId", associatedUserId);
                    break;
            }
        }

        this.msgPushRecordService.createPushTask(triggerStatus, sqlParam, now, otherParam);
        return ApiResult.ok();
    }


    @ApiOperation(value = "获取系统配置")
    @RequestMapping(value = "/api/getSystemConf")
    public ApiResult getSystemConf(@RequestParam("type") Integer type) {
        List<SystemConf> list = this.systemConfService.getByCondition(new SystemConf().setType(type));
        return ApiResult.ok(list.size() > 0 ? list.get(0) : null);
    }


    @ApiOperation(value = "获取企业微信token")
    @RequestMapping(value = "/api/getEnterpriseToken")
    public ApiResult<String> getEnterpriseToken(@RequestParam("corpid") String corpid,
                                                @RequestParam("corpsecret") String corpsecret) {
        JSONObject result = this.wechatMsgService.getEnterpriseToken(corpid, corpsecret, true);
        if (!result.getInt("errcode").equals(0)) {
            return ApiResult.error(result.getStr("errmsg"));
        }
        return ApiResult.ok(result.getStr("access_token"));
    }

    @ApiOperation(value = "获取企业微信部门")
    @RequestMapping(value = "/api/getEnterpriseDep")
    public ApiResult getEnterpriseDep(@RequestParam(value = "departmentId", required = false) Long departmentId,
                                      @RequestParam("corpid") String corpid,
                                      @RequestParam("corpsecret") String corpsecret,
                                      @RequestParam("token") String token) {
        JSONObject result = this.wechatMsgService.getEnterpriseDep(departmentId, corpid, corpsecret, token);
        if (!result.getInt("errcode").equals(0)) {
            return ApiResult.error(result.getStr("errmsg"));
        }
        return ApiResult.ok(result.getJSONArray("department"));
    }

    @ApiOperation(value = "获取企业微信部门员工详情")
    @RequestMapping(value = "/api/getEnterpriseDepStaff")
    public ApiResult getEnterpriseDepStaff(@RequestParam("departmentId") Long departmentId,
                                           @RequestParam("fetchChild") boolean fetchChild,
                                           @RequestParam("corpid") String corpid,
                                           @RequestParam("corpsecret") String corpsecret,
                                           @RequestParam("token") String token) {
        JSONObject result = this.wechatMsgService.getEnterpriseDepStaff(departmentId, fetchChild, corpid, corpsecret, token);
        if (!result.getInt("errcode").equals(0)) {
            return ApiResult.error(result.getStr("errmsg"));
        }
        return ApiResult.ok(result.getJSONArray("userlist"));
    }

    /**
     * 获取腾讯地图经纬度详情
     *
     * @param address
     * @param key
     * @return
     */
    @RequestMapping(value = "/api/getTencentMapLaAndLoInfo")
    public ApiResult getTencentMapLaAndLoInfo(@RequestParam("address") String address,
                                              @RequestParam("key") String key) {
        if (StringUtils.isEmpty(address) || StringUtils.isEmpty(key)) {
            return ApiResult.ok("地址/秘钥不能为空");
        }
        JSONObject response = this.mapPositioningService.getTencentMapLaAndLoInfo(address, key);
        if (!"0".equals(response.getStr("status"))) {
            return ApiResult.error(response.getStr("message"));
        }
        return ApiResult.ok(response.get("result"));

    }

    /**
     * 获取腾讯地图经纬度
     *
     * @param address
     * @param key
     * @return
     */
    @RequestMapping(value = "/api/getTencentMapLaAndLo")
    public ApiResult<MapEntity> getTencentMapLaAndLo(String address, String key) {
        if (StringUtils.isEmpty(address) || StringUtils.isEmpty(key)) {
            return ApiResult.ok("地址/秘钥不能为空");
        }
        MapEntity mapEntity = this.mapPositioningService.getTencentMapLaAndLo(address, key);
        return ApiResult.ok(mapEntity);
    }


    /**
     * 批量更新实时定位GPS
     *
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/api/batchSyncGPSPositioning")
    public ApiResult batchSyncGPSPositioning(@RequestBody Map<String, List<String>> paramMap) {
        if (paramMap == null || paramMap.size() == 0) {
            return ApiResult.ok();
        }
        //车牌
        List<String> licensePlateList = new ArrayList<>();
        List<String> orderNos = new ArrayList<>();
        paramMap.forEach((k, v) -> {
            licensePlateList.add(k);
            orderNos.addAll(v);
        });
        //获取供应商
        List<VehicleDetailsVO> list = vehicleInfoService.getDetailsByPlateNum(licensePlateList);

        Map<String, List<VehicleDetailsVO>> tmp = list.stream().filter(e -> e.getSupplierInfoVO().getGpsType() != null).collect(Collectors.groupingBy(e -> e.getSupplierInfoVO().getGpsType() + "~" + e.getSupplierInfoVO().getSupplierCode()));
        if (tmp == null) {
            log.info("供应商没有配置gps厂商");
            return ApiResult.ok();
        }
        //根据车牌获取gps信息
        List<GpsPositioning> oldPositioning = this.gpsPositioningService.getByOrderNo(orderNos, 1);
        Map<String, GpsPositioning> oldMap = oldPositioning.stream().collect(Collectors.toMap(e -> e.getPlateNumber() + "~" + e.getOrderNo(), e -> e));
        tmp.forEach((k, v) -> {
            String[] split = k.split("~");
            List<String> licensePlates = new ArrayList<>();
            SupplierInfoVO supplierInfoVO = v.get(0).getSupplierInfoVO();
            String gpsReqParam = supplierInfoVO.getGpsReqParam();
            Map<String, Object> paraMap = JSONUtil.toBean(gpsReqParam, Map.class);
            paraMap.put("gpsAddress", supplierInfoVO.getGpsAddress());
            for (VehicleDetailsVO vehicleDetailsVO : v) {
                licensePlates.add(vehicleDetailsVO.getPlateNumber());
            }
            try {
                Object obj = this.gpsPositioningApiService.getPositionsObj(licensePlates, Integer.valueOf(split[0]), paraMap);
                List<GpsPositioning> gpsPositioning = this.gpsPositioningApiService.convertDatas(obj);
                if (gpsPositioning != null) {
                    //一辆车绑定多个订单
                    List<GpsPositioning> appends = new ArrayList<>();
                    gpsPositioning.forEach(e -> {
                        List<String> orderNoList = paramMap.get(e.getPlateNumber());
                        orderNoList.forEach(e1 -> {
                            GpsPositioning convert = ConvertUtil.convert(e, GpsPositioning.class);
                            convert.setOrderNo(e1);
                            appends.add(convert);
                        });

                    });
                    appends.forEach(e -> {
                        GpsPositioning positioning = oldMap.get(e.getPlateNumber() + "~" + e.getOrderNo());
                        if (positioning == null) {
                            e.setCreateTime(LocalDateTime.now()).setStatus(1).setStartTime(LocalDateTime.now()).setEndTime(LocalDateTime.now());
                        } else {
                            e.setId(positioning.getId()).setUpdateTime(LocalDateTime.now()).setStartTime(LocalDateTime.now()).setEndTime(LocalDateTime.now());
                        }
                    });
                    //同步信息
                    this.gpsPositioningService.saveOrUpdateBatch(appends);
                }
            } catch (JayudBizException e) {
                log.error("批量获取实时定位错误,gps厂商={},错误信息={}", GPSTypeEnum.getDesc(Integer.valueOf(split[0])), e.getMessage(), e);
            }
        });

        return ApiResult.ok();
    }


    /**
     * 批量更新GPS历史轨迹
     *
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/api/batchSyncGPSHistoryPositioning")
    public ApiResult batchSyncGPSHistoryPositioning(@RequestBody Map<String, List<Map<String, Object>>> paramMap) {
        if (paramMap == null) {
            return ApiResult.ok();
        }
        //车牌
        List<String> licensePlateList = new ArrayList<>();
        List<String> orderNos = new ArrayList<>();
        paramMap.forEach((k, v) -> {
            licensePlateList.add(k);
            v.forEach(e -> {
                String orderNo = MapUtil.getStr(e, "orderNo");
                orderNos.add(orderNo);
            });
        });
        //获取供应商
        List<VehicleDetailsVO> list = vehicleInfoService.getDetailsByPlateNum(licensePlateList);

        Map<String, List<VehicleDetailsVO>> tmp = list.stream().filter(e -> e.getSupplierInfoVO().getGpsType() != null).collect(Collectors.groupingBy(e -> e.getSupplierInfoVO().getGpsType() + "~" + e.getSupplierInfoVO().getSupplierCode()));
        if (tmp == null) {
            log.info("供应商没有配置gps厂商");
            return ApiResult.ok();
        }

        //根据车牌获取gps信息
//        List<GpsPositioning> oldPositioning = this.gpsPositioningService.getGroupByOrderNo(orderNos, 2);
//        Map<String, GpsPositioning> oldMap = oldPositioning.stream().collect(Collectors.toMap(e -> e.getPlateNumber() + "~" + e.getOrderNo(), e -> e));
        tmp.forEach((k, v) -> {
            String[] split = k.split("~");
            SupplierInfoVO supplierInfoVO = v.get(0).getSupplierInfoVO();
            String gpsReqParam = supplierInfoVO.getGpsReqParam();
            Map<String, Object> paraMap = JSONUtil.toBean(gpsReqParam, Map.class);
            paraMap.put("gpsAddress", supplierInfoVO.getGpsAddress());

            for (VehicleDetailsVO vehicleDetailsVO : v) {
                //获取车牌下订单集合
                List<Map<String, Object>> orderInfos = paramMap.get(vehicleDetailsVO.getPlateNumber());
                orderInfos.forEach(e -> {
                    String orderNo = MapUtil.getStr(e, "orderNo");
                    //过滤已经同步的订单
//                    if (oldMap.get(vehicleDetailsVO.getPlateNumber() + "~" + orderNo) == null) {
                    if (!this.redisUtils.hasKey("GPS_" + orderNo)) {
                        try {
                            LocalDateTime startTime = MapUtil.get(e, "startTime", LocalDateTime.class);
                            LocalDateTime endTime = MapUtil.get(e, "endTime", LocalDateTime.class);
                            Object obj = this.gpsPositioningApiService.getHistory(vehicleDetailsVO.getPlateNumber(), startTime, endTime, Integer.valueOf(split[0]), paraMap);

                            List<GpsPositioning> gpsPositioning = this.gpsPositioningApiService.convertDatas(obj);
                            if (gpsPositioning != null) {
                                List<List<String>> positionList = new ArrayList<>();
                                gpsPositioning.forEach(g -> {
                                    g.setOrderNo(orderNo).setCreateTime(LocalDateTime.now()).setStatus(2).setStartTime(startTime).setEndTime(endTime);
                                    List<String> position = new ArrayList<>();
                                    position.add(g.getLatitude());
                                    position.add(g.getLongitude());
                                    positionList.add(position);
                                });
                                this.redisUtils.set("GPS_" + orderNo, positionList);
//                            this.gpsPositioningService.saveBatch(gpsPositioning);
                            }

                        } catch (JayudBizException exception) {
                            log.error("获取历史轨迹错误,gps厂商={},错误信息={}", GPSTypeEnum.getDesc(Integer.valueOf(split[0])), exception.getMessage(), exception);
                        }
                    }
                });
            }
        });

        return ApiResult.ok();
    }


    /**
     * 根据子订单主键集合查询订单轨迹
     *
     * @param subOrderIds
     * @param status
     * @param type
     * @return
     */
    @RequestMapping(value = "/api/getLogisticsTrackByOrderIds")
    public ApiResult getLogisticsTrackByOrderIds(@RequestParam("subOrderIds") List<Long> subOrderIds,
                                                 @RequestParam("status") List<String> status,
                                                 @RequestParam("type") Integer type) {

        List<LogisticsTrack> list = this.logisticsTrackService.getLogisticsTrackByOrderIds(subOrderIds, status, type);
        return ApiResult.ok(list);
    }


    /**
     * 根据费用类别id获取税率
     *
     * @return
     */
    @RequestMapping(value = "/api/getCostGenreTaxRateByGenreIds")
    public ApiResult<List<CostGenreTaxRate>> getCostGenreTaxRateByGenreIds(@RequestParam("costGenreIds") List<Long> costGenreIds) {
        List<CostGenreTaxRate> list = this.costGenreTaxRateService.getCostGenreTaxRateByGenreIds(costGenreIds);
        return ApiResult.ok(list);
    }


    /**
     * 消息推送
     *
     * @return
     */
    @RequestMapping(value = "/api/channelMsgPush")
    public ApiResult channelMsgPush() {
        try {
            msgPushRecordService.messagePush();
        } catch (Exception e) {
            return ApiResult.error(e.getMessage());
        }
        return ApiResult.ok();
    }

    /**
     * 获取应收出账的费用ids
     *
     * @return
     */
    @RequestMapping(value = "/api/getCostIdsBySubType")
    public ApiResult<List<Long>> getReBillCostIdsBySubType(@RequestParam(value = "legalIds", required = false) List<Long> legalIds,
                                                           @RequestParam(value = "userName", required = false) String userName,
                                                           @RequestParam("subType") String subType) {
        List<Long> costIds = this.receivableCostService.getReBillCostIdsBySubType(userName, legalIds, subType);
        return ApiResult.ok(costIds);
    }

    /**
     * 获取应付出账的费用ids
     *
     * @return
     */
    @RequestMapping(value = "/api/getPayBillCostIdsBySubType")
    public ApiResult<List<Long>> getPayBillCostIdsBySubType(@RequestParam(value = "legalIds", required = false) List<Long> legalIds,
                                                            @RequestParam(value = "userName", required = false) String userName,
                                                            @RequestParam("subType") String subType) {
        List<Long> costIds = this.paymentCostService.getPayBillCostIdsBySubType(userName, legalIds, subType);
        return ApiResult.ok(costIds);
    }


    /**
     * 查询供应商code
     */
    @RequestMapping(value = "/api/getSupplierCodesByName")
    ApiResult<Map<String, String>> getSupplierCodesByName() {
        List<SupplierInfo> supplierInfos = this.supplierInfoService.list();
        Map<String, String> map = supplierInfos.stream().collect(Collectors.toMap(e -> e.getSupplierChName(), e -> e.getSupplierCode()));
        return ApiResult.ok(map);
    }

    /**
     * 保存或更新
     *
     * @param form
     */
    @RequestMapping(value = "/api/saveOrUpdateOutMainOrderForm")
    public ApiResult<Boolean> saveOrUpdateOutMainOrderForm(@RequestBody InputOrderForm form) {
        return ApiResult.ok(orderInfoService.createOrder(form));
    }

    /**
     * 根据业务名称获取业务编码
     */
    @RequestMapping(value = "/api/getProductBizIdCodeByName")
    public ApiResult<String> getProductBizIdCodeByName(@RequestBody String name) {
        return ApiResult.ok(this.productBizService.getProductBizIdCodeByName(name));
    }

    /**
     * 根据客户id查询客户信息VO
     */
    @RequestMapping(value = "/api/getCustomerInfoVOById")
    public ApiResult getCustomerInfoVOById(@RequestParam("id") Long id) {
        CustomerInfoVO customerInfoVO = this.customerInfoService.getCustomerInfoById(id);
        return ApiResult.ok(customerInfoVO);
    }

    /**
     * 根据仓库名称获取仓库ID
     *
     * @param warehouseName
     * @return
     */
    @RequestMapping(value = "/api/getWarehouseIdByName")
    public ApiResult<Long> getWarehouseIdByName(@RequestParam(value = "warehouseName", required = true) String warehouseName) {
        return ApiResult.ok(this.warehouseInfoService.getWarehouseIdByName(warehouseName));
    }

    /**
     * 根据省市区名称列表获取Map
     */
    @RequestMapping(value = "/api/getRegionCityIdMapByName")
    public ApiResult<Map<String, Long>> getRegionCityIdMapByName(@RequestParam(value = "provinceName", required = true) String provinceName,
                                                                 @RequestParam(value = "cityName", required = true) String cityName,
                                                                 @RequestParam(value = "areaName", required = true) String areaName) {

        if (StringUtils.isEmpty(provinceName) || StringUtils.isEmpty(cityName) || StringUtils.isEmpty(areaName)) {
            return ApiResult.error("省市区信息不完整");
        }

        RegionCity province = regionCityService.getProvinceIdByName(provinceName);
        if (province == null) {
            return ApiResult.error("找不到对应的省份信息");
        }

        RegionCity city = regionCityService.getCityOrAreaIdByName(province.getId(), cityName);
        if (city == null) {
            return ApiResult.error("找不到对应的城市信息");
        }

        RegionCity area = regionCityService.getCityOrAreaIdByName(city.getId(), areaName);
        if (area == null) {
            return ApiResult.error("找不到对应的区域信息");
        }

        Map<String, Long> regionCityMap = new HashMap<>();
        regionCityMap.put(provinceName, province.getId());
        regionCityMap.put(cityName, city.getId());
        regionCityMap.put(areaName, area.getId());
        return ApiResult.ok(regionCityMap);
    }

    @ApiOperation(value = "根据客户appid查询私钥信息解密")
    @PostMapping("/api/findClientSecretKeyOne")
    public ApiResult findClientSecretKeyOne(@RequestParam("appId") String appId) {
        ClientSecretKeyVO clientSecretKeyOne = this.clientSecretKeyService.findClientSecretKeyOne(appId);
        return ApiResult.ok(clientSecretKeyOne);
    }

    @ApiOperation(value = "根据客户id查询私钥信息解密")
    @PostMapping("/api/findClientSecretOne")
    public ApiResult findClientSecretOne(@RequestParam("cId") String cId) {
        ClientSecretKeyVO clientSecretKeyOne = this.clientSecretKeyService.findClientSecretOne(cId);
        return ApiResult.ok(clientSecretKeyOne);
    }

    @ApiOperation(value = "根据公钥去查询私钥信息解密")
    @PostMapping("/api/findClientSecretPublicKeyOne")
    public ApiResult findClientSecretPublicKeyOne(@RequestParam("publicKey") String publicKey) {
        ClientSecretKeyVO clientSecretKeyOne = this.clientSecretKeyService.findClientSecretPublicKeyOne(publicKey);
        return ApiResult.ok(clientSecretKeyOne);
    }


    @ApiOperation(value = "根据主订单编号去关闭主订单")
    @PostMapping("/api/deleteOrderInfoByIdOne")
    public ApiResult deleteOrderInfoUpdateByIdOne(@RequestParam("orderNo") String orderNo) {
        Long mainOrderId = orderInfoService.getIdByOrderNo(orderNo);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(mainOrderId);
        orderInfo.setStatus(ProcessStatusEnum.CLOSE.getCode());
        return ApiResult.ok(this.orderInfoService.updateById(orderInfo));
    }

    @ApiOperation(value = "下拉所有车型")
    @PostMapping(value = "/api/initVehicleSize")
    public CommonResult<List<com.jayud.common.entity.InitComboxStrVO>> initVehicleSize() {
        //获取下拉车型
        List<VehicleSizeInfoVO> vehicleSizeInfoVOS = vehicleInfoService.findVehicleSize();
        List<com.jayud.common.entity.InitComboxStrVO> cabinetSizes = new ArrayList<>();
        for (VehicleSizeInfoVO obj : vehicleSizeInfoVOS) {
            com.jayud.common.entity.InitComboxStrVO initComboxVO = new com.jayud.common.entity.InitComboxStrVO();
            initComboxVO.setId(obj.getId());
            initComboxVO.setName(obj.getVehicleSize());
            cabinetSizes.add(initComboxVO);
        }
        return CommonResult.success(cabinetSizes);
    }

    @ApiOperation(value = "录用费用页面-下拉选单位")
    @PostMapping(value = "/api/initCostUnit")
    public CommonResult<List<InitComboxStrVO>> initCostUnit() {
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (UnitEnum unitEnum : UnitEnum.values()) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(unitEnum.getCode());
            comboxStrVO.setName(unitEnum.getDesc());
            comboxStrVOS.add(comboxStrVO);
        }
        return CommonResult.success(comboxStrVOS);
    }

    @ApiOperation(value = "录入费用:应收/付项目/币种 ")
    @PostMapping(value = "/api/initCost")
    public CommonResult initCost(@RequestBody Map<String, Object> param) {
        String createdTimeStr = MapUtil.getStr(param, "createdTimeStr");
        if (StringUtil.isNullOrEmpty(createdTimeStr)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Map<String, Object> result = new HashMap<>();
        List<CostInfo> costInfos = costInfoService.findCostInfo();//费用项目
        List<InitComboxStrVO> paymentCombox = new ArrayList<>();
        List<InitComboxStrVO> receivableCombox = new ArrayList<>();
        for (CostInfo costInfo : costInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(costInfo.getIdCode());
            comboxStrVO.setName(costInfo.getName());
            receivableCombox.add(comboxStrVO);//后期没做应收应付项目的区分
            paymentCombox.add(comboxStrVO);
        }
        result.put("paymentCost", paymentCombox);
        result.put("receivableCost", receivableCombox);

        //币种
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        List<CurrencyInfoVO> currencyInfos = currencyInfoService.findCurrencyInfo(createdTimeStr);
        for (CurrencyInfoVO currencyInfo : currencyInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(currencyInfo.getCurrencyCode());
            comboxStrVO.setName(currencyInfo.getCurrencyName());
            comboxStrVO.setNote(String.valueOf(currencyInfo.getExchangeRate()));
            initComboxStrVOS.add(comboxStrVO);
        }
        result.put("currency", initComboxStrVOS);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "费用类别,idCode=费用名称的隐藏值")
    @PostMapping(value = "/api/initCostType")
    public CommonResult<List<InitComboxVO>> initCostType(@RequestBody Map<String, Object> param) {
        String idCode = MapUtil.getStr(param, CommonConstant.ID_CODE);
        QueryWrapper queryCostInfo = new QueryWrapper();
        queryCostInfo.eq(SqlConstant.ID_CODE, idCode);
        CostInfo costInfo = costInfoService.getOne(queryCostInfo);
        if (costInfo == null || StringUtil.isNullOrEmpty(costInfo.getCids())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        String[] cids = costInfo.getCids().split(CommonConstant.COMMA);
        List<InitComboxVO> costTypeComboxs = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, CommonConstant.VALUE_1);
        queryWrapper.in(SqlConstant.ID, cids);
        List<CostType> costTypes = costTypeService.list(queryWrapper);
        for (CostType costType : costTypes) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setName(costType.getCodeName());
            initComboxVO.setId(costType.getId());
            costTypeComboxs.add(initComboxVO);
        }
        return CommonResult.success(costTypeComboxs);
    }

    @ApiOperation(value = "根据费用名称查询费用类型")
    @PostMapping(value = "/api/initCostTypeByCostInfoCode")
    public CommonResult<Map<String, List<InitComboxVO>>> initCostTypeByCostInfoCode(){
        return CommonResult.success(this.costInfoService.initCostTypeByCostInfoCode());
    }

    @ApiOperation(value = "获取省市区树结构")
    @PostMapping(value = "/api/adrrTree")
    public CommonResult<List<TreeNode>> adrrTree() {
        List<RegionCity> list = this.regionCityService.list();
        List<TreeNode> treeNodes = new ArrayList<>();
        list.forEach(e -> {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(e.getId()).setLabel(e.getName()).setParentId(e.getParentId());
            treeNodes.add(treeNode);
        });
        List<TreeNode> tree = TreeUtil.getTree(treeNodes, 0L);
        return CommonResult.success(tree);
    }
}