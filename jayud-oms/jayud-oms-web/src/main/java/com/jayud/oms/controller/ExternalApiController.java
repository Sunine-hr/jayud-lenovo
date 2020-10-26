package com.jayud.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.model.bo.AuditInfoForm;
import com.jayud.oms.model.bo.HandleSubProcessForm;
import com.jayud.oms.model.bo.InputMainOrderForm;
import com.jayud.oms.model.bo.OprStatusForm;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.po.WarehouseInfo;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.model.vo.InputMainOrderVO;
import com.jayud.oms.model.vo.OrderStatusVO;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        logisticsTrack.setCreatedUser(String.valueOf(getLoginUser().getData()));
        logisticsTrack.setCreatedTime(LocalDateTime.now());
        logisticsTrackService.saveOrUpdate(logisticsTrack);
        return ApiResult.ok();
    }

    /**
     * 获取当前登录用户
     * @return
     */
    @RequestMapping(value = "/api/getLoginUser")
    ApiResult getLoginUser(){
        String loginUser = redisUtils.get("loginUser",100);
        return ApiResult.ok(loginUser);
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
        auditInfo.setAuditTypeDesc(form.getAuditTypeDesc());
        auditInfo.setAuditStatus(form.getAuditStatus());
        auditInfo.setAuditComment(form.getAuditComment());
        auditInfo.setCreatedUser(String.valueOf(getLoginUser().getData()));
        auditInfo.setAuditUser(auditInfo.getCreatedUser());
        auditInfo.setAuditTime(LocalDateTime.now());
        auditInfo.setCreatedTime(LocalDateTime.now());
        boolean result = auditInfoService.save(auditInfo);
        return ApiResult.ok(result);
    }

    @ApiOperation(value = "中转仓库")
    @PostMapping(value = "api/initWarehouseInfo")
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
    @PostMapping(value = "api/initSupplierInfo")
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


}









    



