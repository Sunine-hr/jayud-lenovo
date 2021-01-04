package com.jayud.oms.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.utils.BeanUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
@Api(tags = "oms对外接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    IOrderInfoService orderInfoService;

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


    @ApiOperation(value = "保存主订单")
    @RequestMapping(value = "/api/oprMainOrder")
    public ApiResult oprMainOrder(@RequestBody InputMainOrderForm form) {
        String result = orderInfoService.oprMainOrder(form);
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
        logisticsTrack.setOperatorTime(LocalDateTime.now());
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
        auditInfo.setAuditTime(LocalDateTime.now());
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

    @ApiOperation(value = "下拉框:获取审核通过的车辆供应商")
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
     * 应付暂存
     */
    @RequestMapping(value = "/api/oprCostBill")
    ApiResult<Boolean> oprCostBill(@RequestBody OprCostBillForm form) {
        Boolean result = false;
        if ("payment".equals(form.getOprType())) {
            List<OrderPaymentCost> paymentCosts = new ArrayList<>();
            if (form.getCmd().contains("pre")) {//暂存应收
                List<OrderPaymentCost> delCosts = new ArrayList<>();
                for (Long costId : form.getCostIds()) {
                    OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                    orderPaymentCost.setId(costId);
                    orderPaymentCost.setIsBill("1");//暂存
                    paymentCosts.add(orderPaymentCost);
                }
                //获取现存数据有多少暂存的，改为未出账
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("is_bill","1");
                List<OrderPaymentCost> existPaymentCosts = paymentCostService.list(queryWrapper);
                for (OrderPaymentCost existPaymentCost : existPaymentCosts) {
                    OrderPaymentCost delCost = new OrderPaymentCost();
                    delCost.setId(existPaymentCost.getId());
                    delCost.setIsBill("0");//未出账
                    delCosts.add(delCost);
                }
                //把原来暂存的清除,更新未出账
                if(delCosts.size() > 0){
                    paymentCostService.updateBatchById(delCosts);
                }
            }else if("del".equals(form.getCmd())){//删除对账单
                for (Long costId : form.getCostIds()) {
                    OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                    orderPaymentCost.setId(costId);
                    orderPaymentCost.setIsBill("0");//未出账
                    paymentCosts.add(orderPaymentCost);
                }
            }else{//生成应收账单
                for (Long costId : form.getCostIds()) {
                    OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                    orderPaymentCost.setId(costId);
                    orderPaymentCost.setIsBill("2");//生成对账单
                    paymentCosts.add(orderPaymentCost);
                }
            }
            result = paymentCostService.updateBatchById(paymentCosts);
        }else if("receivable".equals(form.getOprType())){
            List<OrderReceivableCost> receivableCosts = new ArrayList<>();
            if(form.getCmd().contains("pre")){//暂存应付
                List<OrderReceivableCost> delCosts = new ArrayList<>();
                for (Long costId : form.getCostIds()) {
                    OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                    orderReceivableCost.setId(costId);
                    orderReceivableCost.setIsBill("1");//暂存
                    receivableCosts.add(orderReceivableCost);
                }
                //获取现存数据有多少暂存的，改为未出账
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("is_bill","1");
                List<OrderReceivableCost> existReceivableCosts = receivableCostService.list(queryWrapper);
                for (OrderReceivableCost existReceivableCost : existReceivableCosts) {
                    OrderReceivableCost delCost = new OrderReceivableCost();
                    delCost.setId(existReceivableCost.getId());
                    delCost.setIsBill("0");//未出账
                    delCosts.add(delCost);
                }
                //把原来暂存的清除,更新未出账
                if(delCosts.size() > 0) {
                    receivableCostService.updateBatchById(delCosts);
                }
            }else if("del".equals(form.getCmd())){//删除对账单
                for (Long costId : form.getCostIds()) {
                    OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                    orderReceivableCost.setId(costId);
                    orderReceivableCost.setIsBill("0");//未出账
                    receivableCosts.add(orderReceivableCost);
                }
            }else{//生成应付账单
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
    public ApiResult editSaveConfirm(@RequestParam("costIds") List<Long> costIds,@RequestParam("oprType") String oprType,
                                     @RequestParam("cmd") String cmd){
        if("save_confirm".equals(cmd)) {
            if ("receivable".equals(oprType)) {
                OrderReceivableCost receivableCost = new OrderReceivableCost();
                receivableCost.setIsBill("save_confirm");//持续操作中的过度状态
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.in("id", costIds);
                receivableCostService.update(receivableCost, updateWrapper);
            } else if ("payment".equals(oprType)) {
                OrderPaymentCost paymentCost = new OrderPaymentCost();
                paymentCost.setIsBill("save_confirm");//持续操作中的过度状态
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.in("id", costIds);
                paymentCostService.update(paymentCost, updateWrapper);
            }
        }else if("edit_del".equals(cmd)){
            if ("receivable".equals(oprType)) {
                OrderReceivableCost receivableCost = new OrderReceivableCost();
                receivableCost.setIsBill("0");//从save_confirm状态回滚到未出账-0状态
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.in("id", costIds);
                receivableCostService.update(receivableCost, updateWrapper);
            } else if ("payment".equals(oprType)) {
                OrderPaymentCost paymentCost = new OrderPaymentCost();
                paymentCost.setIsBill("0");//从save_confirm状态回滚到未出账-0状态
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.in("id", costIds);
                paymentCostService.update(paymentCost, updateWrapper);
            }
        }
        return ApiResult.ok(true);
    }

    /**
     * 提交财务审核时，财务可能编辑费用类型
     * @param forms
     * @param cmd
     * @return
     */
    @RequestMapping(value = "api/oprCostGenreByCw")
    ApiResult<Boolean> oprCostGenreByCw(@RequestBody List<OrderCostForm> forms,@RequestParam("cmd") String cmd){
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
     * @param forms
     * @param cmd
     * @return
     */
    @RequestMapping(value = "api/writeBackCostData")
    ApiResult writeBackCostData(@RequestBody List<OrderCostForm> forms, @RequestParam("cmd") String cmd){
        if ("receivable".equals(cmd)) {
            for (OrderCostForm orderCost : forms) {
                //获取该条费用以出账时结算币种的汇率和本币金额
                InputReceivableCostVO sCost = receivableCostService.getWriteBackSCostData(orderCost.getCostId());
                //汇率校验
                if(sCost.getExchangeRate() == null || sCost.getExchangeRate().compareTo(new BigDecimal("0")) == 0){
                    return ApiResult.error(10001,"请配置原始币种:"+sCost.getOCurrencyName()+",兑换币种:人民币的汇率");
                }
                OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                orderReceivableCost.setId(orderCost.getCostId());
                orderReceivableCost.setExchangeRate(sCost.getExchangeRate());//汇率
                orderReceivableCost.setChangeAmount(sCost.getChangeAmount());//本币金额
                orderReceivableCost.setOptName(orderCost.getLoginUserName());
                orderReceivableCost.setOptTime(LocalDateTime.now());
                receivableCostService.updateById(orderReceivableCost);
            }
        } else if ("payment".equals(cmd)) {
            for (OrderCostForm orderCost : forms) {
                //获取该条费用以出账时结算币种的汇率和本币金额
                InputPaymentCostVO fCost = paymentCostService.getWriteBackFCostData(orderCost.getCostId());
                //汇率校验
                if(fCost.getExchangeRate() == null || fCost.getExchangeRate().compareTo(new BigDecimal("0")) == 0){
                    return ApiResult.error(10001,"请配置原始币种:"+fCost.getOCurrencyName()+",兑换币种:人民币的汇率");
                }
                OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                orderPaymentCost.setId(orderCost.getCostId());
                orderPaymentCost.setExchangeRate(fCost.getExchangeRate());//汇率
                orderPaymentCost.setChangeAmount(fCost.getChangeAmount());//本币金额
                orderPaymentCost.setOptName(orderCost.getLoginUserName());
                orderPaymentCost.setOptTime(LocalDateTime.now());
                paymentCostService.updateById(orderPaymentCost);
            }
        }
        return ApiResult.ok();
    }

    /**
     * 获取所有可用的费用类型
     * @return
     */
    @RequestMapping(value = "api/findEnableCostGenre")
    ApiResult<List<InitComboxVO>> findEnableCostGenre(){
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
     * 根据编码获取港口名称
     */
    @RequestMapping(value = "/api/getPortCodeByName")
    public ApiResult getPortCodeByName(@RequestBody String name) {
        PortInfo portInfo = new PortInfo().setName(name);
        List<PortInfo> portInfos = this.portInfoService.findPortInfoByCondition(portInfo);
        return ApiResult.ok(portInfos.size() > 0 ? portInfos.get(0).getIdCode() : null);
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


    @ApiOperation("根据订单id集合查询商品信息")
    @RequestMapping(value = "/api/getGoodsByBusIds")
    public ApiResult getGoodsByBusIds(@RequestParam("orderId") List<Long> orderId,
                                      @RequestParam("businessType") Integer businessType) {
        //查询商品信息
        List<Goods> goods = this.goodsService.getGoodsByBusIds(orderId, businessType);
        return ApiResult.ok(goods);
    }


}









    



