package com.jayud.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@Api(tags = "oms对外接口")
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

    @ApiOperation(value = "保存主订单")
    @RequestMapping(value = "/api/oprMainOrder")
    public ApiResult oprMainOrder(@RequestBody InputMainOrderForm form) {
        String result = orderInfoService.oprMainOrder(form);
        if(result != null){
            return ApiResult.ok(result);
        }
        return ApiResult.error();
    }


    @ApiOperation(value = "获取主订单信息")
    @RequestMapping(value = "/api/getMainOrderById")
    ApiResult getMainOrderById(@RequestParam(value = "id") Long id){
        InputMainOrderVO inputOrderVO = orderInfoService.getMainOrderById(id);
        return ApiResult.ok(inputOrderVO);
    }

    @ApiOperation(value = "获取主订单ID")
    @RequestMapping(value = "/api/getIdByOrderNo")
    ApiResult getIdByOrderNo(@RequestParam(value = "orderNo") String orderNo){
        Long mainOrderId = orderInfoService.getIdByOrderNo(orderNo);
        return ApiResult.ok(mainOrderId);
    }


    @ApiOperation(value = "保存操作状态")
    @RequestMapping(value = "/api/saveOprStatus")
    ApiResult saveOprStatus(@RequestBody OprStatusForm form){
        LogisticsTrack logisticsTrack = new LogisticsTrack();
        logisticsTrack.setMainOrderId(form.getMainOrderId());
        logisticsTrack.setOrderId(form.getOrderId());
        logisticsTrack.setStatus(form.getStatus());
        logisticsTrack.setStatusName(form.getStatusName());
        logisticsTrack.setOperatorUser(form.getOperatorUser());
        logisticsTrack.setOperatorTime(DateUtils.str2LocalDateTime(form.getOperatorTime(),DateUtils.DATE_TIME_PATTERN));
        logisticsTrack.setStatusPic(form.getStatusPic());
        logisticsTrack.setDescription(form.getDescription());
        logisticsTrack.setEntrustNo(form.getEntrustNo());
        logisticsTrack.setGoCustomsTime(DateUtils.str2LocalDateTime(form.getGoCustomsTime(),DateUtils.DATE_TIME_PATTERN));
        logisticsTrack.setPreGoCustomsTime(DateUtils.str2LocalDateTime(form.getPreGoCustomsTime(),DateUtils.DATE_TIME_PATTERN));
        logisticsTrack.setCreatedUser(UserOperator.getToken());
        logisticsTrack.setCreatedTime(LocalDateTime.now());
        logisticsTrackService.saveOrUpdate(logisticsTrack);
        return ApiResult.ok();
    }


    /**
     * 子订单流程
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/handleSubProcess")
    ApiResult handleSubProcess(@RequestBody HandleSubProcessForm form){
        List<OrderStatusVO> orderStatusVOS = orderInfoService.handleSubProcess(form);
        return ApiResult.ok(orderStatusVOS);
    }

    /**
     * 记录审核信息
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/saveAuditInfo")
    ApiResult<Boolean> saveAuditInfo(@RequestBody AuditInfoForm form){
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
        queryWrapper.eq(SqlConstant.STATUS,1);
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

    @ApiOperation(value = "车辆供应商")
    @RequestMapping(value = "api/initSupplierInfo")
    public CommonResult initSupplierInfo() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS,1);
        List<SupplierInfo> supplierInfos = supplierInfoService.list(queryWrapper);
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
     * @param orderId
     * @return
     */
    @RequestMapping(value = "api/delOprStatus")
    ApiResult delOprStatus(@RequestParam("orderId") Long orderId){
        logisticsTrackService.removeById(orderId);
        return ApiResult.ok();
    }

    /**
     * 删除特定单的操作流程
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/delSpecOprStatus")
    ApiResult delSpecOprStatus(@RequestBody DelOprStatusForm form){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.ORDER_ID,form.getOrderId());
        queryWrapper.in(SqlConstant.STATUS,form.getStatus());
        logisticsTrackService.remove(queryWrapper);
        return ApiResult.ok();
    }

    @ApiOperation(value = "初始化司机下拉框")
    @RequestMapping(value = "api/initDriver")
    public CommonResult initDriver() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS,1);
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
     * @return
     */
    @RequestMapping(value = "/api/initDriverInfo")
    ApiResult initDriverInfo(@RequestParam("driverId") Long driverId){
        DriverInfoLinkVO driverInfo = driverInfoService.getDriverInfoLink(driverId);
        return ApiResult.ok(driverInfo);
    }

    /**
     * 应付暂存
     */
    @RequestMapping(value = "/api/oprCostBill")
    ApiResult<Boolean> oprCostBill(@RequestBody OprCostBillForm form){
        Boolean result = false;
        if("payment".equals(form.getOprType())){
            List<OrderPaymentCost> paymentCosts = new ArrayList<>();
            if(form.getCmd().contains("pre")){//暂存应收
                List<OrderPaymentCost> delCosts = new ArrayList<>();
                for (Long costId : form.getCostIds()) {
                    OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
                    orderPaymentCost.setId(costId);
                    orderPaymentCost.setIsBill("1");//暂存
                    paymentCosts.add(orderPaymentCost);

                    OrderPaymentCost delCost = new OrderPaymentCost();
                    delCost.setId(costId);
                    delCost.setIsBill("0");//未出账
                    delCosts.add(delCost);
                }
                //把原来暂存的清除,更新未出账
                paymentCostService.updateBatchById(delCosts);
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

                    OrderReceivableCost delCost = new OrderReceivableCost();
                    delCost.setId(costId);
                    delCost.setIsBill("0");//未出账
                    delCosts.add(delCost);
                }
                //把原来暂存的清除,更新未出账
                receivableCostService.updateBatchById(delCosts);
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
        queryWrapper.eq(SqlConstant.STATUS,1);
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
        queryWrapper.eq(SqlConstant.STATUS,1);
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
     * @param costIds
     * @param oprType 应付还是应收
     * @return
     */
    @ApiOperation(value = "编辑保存确定")
    @RequestMapping(value = "api/editSaveConfirm")
    public ApiResult editSaveConfirm(@RequestParam(value = "costIds") List<Long> costIds,@RequestParam(value = "oprType") String oprType,
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


}









    



