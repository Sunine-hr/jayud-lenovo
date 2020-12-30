package com.jayud.airfreight.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.airfreight.feign.OauthClient;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.model.vo.AirOrderFormVO;
import com.jayud.airfreight.model.vo.AirOrderVO;
import com.jayud.airfreight.model.vo.GoodsVO;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jayud.common.enums.OrderStatusEnum.AIR_A_2;

/**
 * <p>
 * 空运订单表 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@RestController
@Slf4j
@Api(tags = "空运订单")
@RequestMapping("/airOrder")
public class AirOrderController {
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private IAirOrderService airOrderService;
    @Autowired
    private OauthClient oauthClient;
//    @Autowired
//    private IGoodsService goodsService;

    @ApiOperation(value = "分页查询空运订单")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<AirOrderFormVO>> findByPage(@RequestBody QueryAirOrderForm form) {

        //模糊查询客户信息
        if (!StringUtils.isEmpty(form.getCustomerName())) {
            ApiResult result = omsClient.getByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null) {
                JSONArray mainOrders = JSONArray.parseArray(JSON.toJSONString(data));
                form.assemblyMainOrderNo(mainOrders);
            }
        }

        //获取下个节点状态
//        OrderStatusEnum statusEnum = OrderStatusEnum.getAirOrderPreStatus(form.getStatus());

//        form.setStatus(statusEnum == null ? null : statusEnum.getCode());

        IPage<AirOrderFormVO> page = this.airOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success(new CommonPageResult<>(page));
        }

        List<AirOrderFormVO> records = page.getRecords();
        List<Long> airOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        for (AirOrderFormVO record : records) {
            airOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalId());
        }
        //查询商品信息
        List<GoodsVO> goods = this.omsClient.getGoodsByBusIds(airOrderIds, BusinessTypeEnum.KY.getCode()).getData();
        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }

        //查询客户信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (AirOrderFormVO record : records) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);
        }
        return CommonResult.success(new CommonPageResult(page));
    }


    //操作指令,cmd = 1-空运接单,2-空运订舱,3-订单入仓," +
    //            "4-确认提单,5-确认离港，6-确认到港,7-海外代理,8-确认签收
    @ApiOperation(value = "执行空运流程操作")
    @PostMapping(value = "/doAirProcessOpt")
    public CommonResult doAirProcessOpt(@RequestBody AirProcessOptForm form) {
        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("空运订单编号/空运订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //空运订单信息
        AirOrder airOrder = this.airOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(airOrder.getProcessStatus())) {
            return CommonResult.error(400, "该订单已经完成");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(airOrder.getProcessStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }
        OrderStatusEnum statusEnum = OrderStatusEnum.getAirOrderNextStatus(airOrder.getStatus());
        if (statusEnum == null) {
            log.error("执行空运流程操作失败,超出流程之外 data={}", airOrder.toString());
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //校验参数
        form.checkProcessOpt(statusEnum);
        form.setStatus(statusEnum.getCode());

        //指令操作
        switch (statusEnum) {
            case AIR_A_1: //空运接单
                this.airOrderService.updateProcessStatus(new AirOrder()
                                .setOrderTaker(form.getOperatorUser())
                                .setReceivingOrdersDate(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN))
                        , form);
                break;
            case AIR_A_2: //订舱
                this.airOrderService.doAirBookingOpt(form);
                break;
            case AIR_A_3: //订单入仓
                //是否能入仓
//                if (!this.airOrderService.isWarehousing(airOrder)) {
//                    return CommonResult.error(400, "订舱待确认,无法入仓");
//                }
                this.airOrderService.updateProcessStatus(new AirOrder(), form);
                break;
            case AIR_A_4: //确认提单
            case AIR_A_5: //确认离港
            case AIR_A_6: //确认到港
                this.airOrderService.doAirBookingOpt(form);
                break;
            case AIR_A_7: //海外代理
                StringBuilder sb = new StringBuilder();
                form.getProxyServiceType().forEach(e -> sb.append(e).append(","));
                this.airOrderService.updateProcessStatus(new AirOrder()
                        .setOverseasSuppliersId(form.getAgentSupplierId())
                        .setProxyServiceType(sb.substring(0, sb.length() - 1)), form);
                break;
            case AIR_A_8: //确认签收
                this.airOrderService.updateProcessStatus(new AirOrder(), form);
                break;
        }
        //发送跟踪信息
        this.airOrderService.trackingPush(form.getOrderId());
        return CommonResult.success();
    }


//    @ApiOperation(value = "订舱驳回 id=空运订单id")
//    @PostMapping(value = "/bookingRejected")
//    public CommonResult bookingRejected(@RequestBody Map<String, Object> form) {
//        Long airOrderId = MapUtil.getLong(form, "id");
//        if (airOrderId == null) {
//            return CommonResult.error(ResultEnum.PARAM_ERROR);
//        }
//
//        AirOrder airOrder = this.airOrderService.getById(airOrderId);
//
//        OrderStatusEnum statusEnum = OrderStatusEnum.getAirOrderRejection(airOrder.getStatus());
//        if (statusEnum == null) {
//            log.warn("当前订单状态无法进行驳回操作 status={}", OrderStatusEnum.getDesc(airOrder.getStatus()));
//            return CommonResult.error(400, "当前订单状态无法进行驳回操作");
//        }
//        switch (statusEnum) {
//            case AIR_A_2_1:
//
//        }
//
//        //修改空运订单状态为订舱驳回状态
//        this.airOrderService.bookingRejected(airOrder);
//        return CommonResult.success();
//    }


    @ApiOperation(value = "订舱驳回编辑 id=空运订单id")
    @PostMapping(value = "/bookingRejectionEdit")
    public CommonResult bookingRejectionEdit(@RequestBody AirProcessOptForm form) {
        if (form.getMainOrderId() == null
                || form.getOrderId() == null
                || form.getAirBooking().getId() == null) {
            log.warn("空运订单编号/空运订单id必填/空运订舱id必填 data={}", JSONUtil.toJsonStr(form));
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }

        AirOrder airOrder = this.airOrderService.getById(form.getOrderId());
        if (!OrderStatusEnum.AIR_A_3_2.getCode().equals(airOrder.getStatus())) {
            log.warn("当前订单状态无法进行操作 status={}", OrderStatusEnum.getDesc(airOrder.getStatus()));
            return CommonResult.error(400, "当前订单状态无法进行操作");
        }
        form.setStatus(AIR_A_2.getCode());
        //校验参数
        form.checkProcessOpt(AIR_A_2);
        //执行订舱驳回编辑
        this.airOrderService.doAirBookingOpt(form);
        return CommonResult.success();
    }


    @ApiOperation(value = "查询订单详情 airOrderId=空运订单id")
    @PostMapping(value = "/getAirOrderDetails")
    public CommonResult<AirOrderVO> getAirOrderDetails(@RequestBody Map<String, Object> map) {
        Long airOrderId = MapUtil.getLong(map, "airOrderId");
        if (airOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        AirOrderVO airOrderDetails = this.airOrderService.getAirOrderDetails(airOrderId);
        return CommonResult.success(airOrderDetails);
    }

    @ApiOperation(value = "空运订单驳回")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody AirCargoRejected airCargoRejected) {
        //查询空运订单
        AirOrder tmp = this.airOrderService.getById(airCargoRejected.getAirOrderId());
        //获取相应驳回操作
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getAirOrderRejection(tmp.getStatus());
        if (orderStatusEnum == null) {
            return CommonResult.error(400, "驳回操作失败,没有相对应的操作");
        }
        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(airCargoRejected.getAirOrderId());
        auditInfoForm.setExtDesc(SqlConstant.AIR_ORDER);
        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());

        auditInfoForm.setAuditComment(airCargoRejected.getCause());

        Integer rejectOptions = airCargoRejected.getRejectOptions() == null ? 1 : airCargoRejected.getRejectOptions();
        airCargoRejected.setRejectOptions(rejectOptions);
        switch (orderStatusEnum) {
            case AIR_A_1_1:
                //订单驳回
                this.airOrderService.orderReceiving(tmp, auditInfoForm, airCargoRejected);
                break;
            case AIR_A_2_1:
            case AIR_A_3_1:
                this.airOrderService.rejectedOpt(tmp, auditInfoForm, airCargoRejected);
                break;
        }

        return CommonResult.success();
    }
}

