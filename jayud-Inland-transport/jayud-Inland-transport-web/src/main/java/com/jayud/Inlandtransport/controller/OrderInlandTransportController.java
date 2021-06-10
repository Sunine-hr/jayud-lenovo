package com.jayud.Inlandtransport.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.Inlandtransport.feign.OauthClient;
import com.jayud.Inlandtransport.feign.OmsClient;
import com.jayud.Inlandtransport.model.bo.ProcessOptForm;
import com.jayud.Inlandtransport.model.bo.QueryOrderForm;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.*;
import com.jayud.Inlandtransport.service.IOrderInlandSendCarsService;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.aop.annotations.DynamicHead;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.AuditInfoForm;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
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

import java.util.*;

import static com.jayud.common.enums.OrderStatusEnum.getInlandTPStatus;

/**
 * <p>
 * 内陆订单 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
@RestController
@RequestMapping("/orderInlandTransport")
@Slf4j
@Api(tags = "内陆运输订单模块")
public class OrderInlandTransportController {

    @Autowired
    private OmsClient omsClient;
    @Autowired
    private IOrderInlandTransportService orderInlandTransportService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IOrderInlandSendCarsService orderInlandSendCarsService;

    @ApiOperation(value = "分页查询订单")
    @PostMapping(value = "/findByPage")
    @DynamicHead
    public CommonResult<CommonPageResult<OrderInlandTransportFormVO>> findByPage(@RequestBody QueryOrderForm form) {

        //模糊查询客户信息
        if (!StringUtils.isEmpty(form.getCustomerName())) {
            ApiResult result = omsClient.getByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null && ((List) data).size() > 0) {
                JSONArray mainOrders = new JSONArray(data);
                form.assemblyMainOrderNo(mainOrders);
            } else {
                form.setMainOrderNos(Collections.singletonList("-1"));
            }
        }
        if (!StringUtils.isEmpty(form.getStatus())) {
            OrderStatusEnum statusEnum = OrderStatusEnum.getInlandTPOrderPreStatus(form.getStatus());
            form.setStatus(statusEnum.getCode());
        }

        if (CollectionUtils.isNotEmpty(form.getTakeTimeStr())) {
            Set<Long> subOrderIds = this.omsClient.getOrderAddressOrderIdByTimeInterval(form.getTakeTimeStr(), OrderAddressEnum.PICK_UP.getCode(), BusinessTypeEnum.NL.getCode()).getData();
            form.setSubOrderIds(subOrderIds);
        }


        IPage<OrderInlandTransportFormVO> page = this.orderInlandTransportService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success(new CommonPageResult<>(page));
        }


        return CommonResult.success(new CommonPageResult(page));
    }


//    @ApiOperation(value = "批量执行程操作")
//    @PostMapping(value = "/doProcessOpt")
//    public CommonResult doBatchProcessOpt(@RequestBody List<ProcessOptForm> form) {
//        form.forEach(this::doProcessOpt);
//        return CommonResult.success();
//    }

    @ApiOperation(value = "执行程操作")
    @PostMapping(value = "/doProcessOpt")
    public CommonResult doProcessOpt(@RequestBody ProcessOptForm form) {
        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("主/子订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //空运订单信息
        OrderInlandTransport order = this.orderInlandTransportService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(order.getProcessStatus())) {
            return CommonResult.error(400, "订单号为" + order.getOrderNo() + "已经完成操作");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(order.getProcessStatus())) {
            return CommonResult.error(400, "订单号为" + order.getOrderNo() + "无法操作");
        }
        //节点处理
        String nextStatus = nodeProcessing(order);
        //校验参数
        OrderStatusEnum statusEnum = OrderStatusEnum.getEnums(nextStatus);
        form.checkProcessOpt(statusEnum);
        form.setStatus(nextStatus);

        //指令操作
        switch (statusEnum) {
            case INLANDTP_NL_1: //接单
                this.orderInlandTransportService.updateProcessStatus(new OrderInlandTransport()
                                .setOrderTaker(form.getOperatorUser())
                                .setReceivingOrdersDate(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN))
                        , form);
                break;
            case INLANDTP_NL_2: //派车
                //查询车辆信息
                Object data = omsClient.getVehicleInfoByIds(Collections.singletonList(form.getSendCarForm().getVehicleId())).getData();
                form.getSendCarForm().setLicensePlate(new JSONArray(data).getJSONObject(0).getStr("plateNumber"));
                form.getSendCarForm().setOrderNo(order.getOrderNo()).setOrderId(order.getId());
                this.orderInlandTransportService.doDispatchOpt(form);
                break;
            case INLANDTP_NL_3: //派车审核
            case INLANDTP_NL_4: //确认派车
            case INLANDTP_NL_5: //车辆提货
            case INLANDTP_NL_6: //货物签收
                this.orderInlandTransportService.updateProcessStatus(new OrderInlandTransport(), form);
                break;
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "查询订单详情 subOrderId=子订单id")
    @PostMapping(value = "/getOrderDetails")
    public CommonResult<OrderInlandTransportDetails> getOrderDetails(@RequestBody Map<String, Object> map) {
        Long subOrderId = MapUtil.getLong(map, "id");
        if (subOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OrderInlandTransportDetails orderDetails = this.orderInlandTransportService.getOrderDetails(subOrderId);
        return CommonResult.success(orderDetails);
    }


    @ApiOperation(value = "查询内陆状态")
    @PostMapping(value = "/initStatus")
    public List<InitComboxStrVO> initStatus() {
        List<OrderStatusEnum> enums = getInlandTPStatus(true);
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (OrderStatusEnum statusEnum : enums) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(statusEnum.getDesc());
            initComboxStrVO.setCode(statusEnum.getCode());
            initComboxStrVOS.add(initComboxStrVO);
        }
        return initComboxStrVOS;
    }

    @ApiOperation(value = "订单驳回")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody OrderRejectedOpt rejectedOpt) {
        //查询订单
        OrderInlandTransport tmp = this.orderInlandTransportService.getById(rejectedOpt.getOrderId());
        //获取相应驳回操作
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getInlandTPOrderRejection(tmp.getStatus());
        if (orderStatusEnum == null) {
            return CommonResult.error(400, "驳回操作失败,没有相对应的操作");
        }
        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(rejectedOpt.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.INLAND_ORDER);
        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());

        auditInfoForm.setAuditComment(rejectedOpt.getCause());

        Integer rejectOptions = rejectedOpt.getRejectOptions() == null ? 1 : rejectedOpt.getRejectOptions();
        rejectedOpt.setRejectOptions(rejectOptions);
        switch (orderStatusEnum) {
            case INLANDTP_NL_1_1:
                //接单驳回
                this.orderInlandTransportService.orderReceiving(tmp, auditInfoForm, rejectedOpt);
                break;
            case INLANDTP_NL_2_1:
            case INLANDTP_NL_3_2:
            case INLANDTP_NL_4_1:
            case INLANDTP_NL_5_1:
                this.orderInlandTransportService.rejectedOpt(tmp, auditInfoForm, rejectedOpt);
                break;
        }

        return CommonResult.success();
    }


    @ApiOperation(value = "派车审核不通过 orderId=子订单id,describes=审核意见")
    @PostMapping(value = "/dispatchReviewFailed")
    public CommonResult dispatchReviewFailed(@RequestBody Map<String, Object> map) {
        Long orderId = MapUtil.getLong(map, "orderId");
        String describes = MapUtil.getStr(map, "describes");
        if (orderId == null) {
            return CommonResult.error(400, "该订单不存在");
        }
        if (StringUtils.isEmpty(describes)) {
            return CommonResult.error(400, "请填写审核意见");
        }
        //查询订单
        OrderInlandTransport tmp = this.orderInlandTransportService.getById(orderId);

        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(orderId);
        auditInfoForm.setExtDesc(SqlConstant.INLAND_ORDER);
        auditInfoForm.setAuditStatus(OrderStatusEnum.INLANDTP_NL_3_1.getCode());
        auditInfoForm.setAuditTypeDesc(OrderStatusEnum.INLANDTP_NL_3_1.getCode());

        auditInfoForm.setAuditComment(describes);

        OrderRejectedOpt rejectedOpt = new OrderRejectedOpt();
        rejectedOpt.setCause(describes);
        rejectedOpt.setDeleteStatusList(Collections.singletonList(OrderStatusEnum.INLANDTP_NL_2.getCode()));
        rejectedOpt.setOrderId(orderId);
        rejectedOpt.setRejectOptions(2);
        this.orderInlandTransportService.rejectedOpt(tmp, auditInfoForm, rejectedOpt);

        return CommonResult.success();
    }


    private String nodeProcessing(OrderInlandTransport order) {
        if (OrderStatusEnum.INLANDTP_NL_3_1.getCode().equals(order.getStatus())) {
            return OrderStatusEnum.INLANDTP_NL_2.getCode();
        }
        //查询下一步节点
        String nextStatus = omsClient.getOrderProcessNode(order.getMainOrderNo()
                , order.getOrderNo(), order.getStatus()).getData();
        if (nextStatus == null) {
            log.error("执行订单流程操作失败,超出流程之外 data={}", order.toString());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        return nextStatus;
    }

}

