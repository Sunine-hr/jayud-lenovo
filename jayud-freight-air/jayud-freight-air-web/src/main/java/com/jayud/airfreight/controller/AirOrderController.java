package com.jayud.airfreight.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.airfreight.feign.FileClient;
import com.jayud.airfreight.feign.OauthClient;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.model.enums.ExceptionCausesEnum;
import com.jayud.airfreight.model.po.AirExtensionField;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.model.vo.AirOrderFormVO;
import com.jayud.airfreight.model.vo.AirOrderVO;
import com.jayud.airfreight.model.vo.GoodsVO;
import com.jayud.airfreight.service.IAirExtensionFieldService;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.*;
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

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.jayud.common.enums.OrderStatusEnum.*;

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
    @Autowired
    private IAirExtensionFieldService airExtensionFieldService;
    @Autowired
    private FileClient fileClient;

    @ApiOperation(value = "分页查询空运订单")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<AirOrderFormVO>> findByPage(@RequestBody QueryAirOrderForm form) {

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
        List<Long> supplierIds = new ArrayList<>();
        for (AirOrderFormVO record : records) {
            airOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            supplierIds.add(record.getSupplierId());
        }
        //查询商品信息
        List<GoodsVO> goods = this.omsClient.getGoodsByBusIds(airOrderIds, BusinessTypeEnum.KY.getCode()).getData();
        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }
        //查询供应商信息
        JSONArray supplierInfo = null;
        if (CollectionUtils.isNotEmpty(supplierIds)) {
            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
        }


        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (AirOrderFormVO record : records) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);
            //拼装供应商
            record.assemblySupplierInfo(supplierInfo);
        }
        return CommonResult.success(new CommonPageResult(page));
    }


    @ApiOperation(value = "批量执行程操作")
    @PostMapping(value = "/doBatchProcessOpt")
    public CommonResult doBatchProcessOpt(@RequestBody Map<String, Object> map) {
        JSONArray processOpts = MapUtil.get(map, "processOpts", JSONArray.class);
        if (processOpts == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<AirOrderFormVO> tmp = JSONUtil.toList(processOpts, AirOrderFormVO.class);
        List<AirProcessOptForm> list = new ArrayList<>();
        tmp.forEach(e -> {
            AirProcessOptForm form = new AirProcessOptForm();
            form.setOrderId(e.getId());
            form.setMainOrderId(e.getMainOrderId());
            form.setOperatorTime(DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN));
            list.add(form);
        });
        list.forEach(this::doAirProcessOpt);
        return CommonResult.success();
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
            return CommonResult.error(400, "订单号为" + airOrder.getOrderNo() + "已经完成操作");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(airOrder.getProcessStatus())) {
            return CommonResult.error(400, "订单号为" + airOrder.getOrderNo() + "无法操作");
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
                String proxyServiceType = sb.length() == 0 ? null : sb.substring(0, sb.length() - 1);
                this.airOrderService.updateProcessStatus(new AirOrder()
                        .setOverseasSuppliersId(form.getAgentSupplierId())
                        .setProxyServiceType(proxyServiceType), form);
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


    @ApiOperation(value = "获取异常原因 createUserType=创建人的类型(0:本系统,1:vivo)")
    @PostMapping(value = "/getExceptionCauses")
    public CommonResult<List<InitComboxVO>> getExceptionCauses(@RequestBody Map<String, Object> map) {
        Integer createUserType = MapUtil.getInt(map, "createUserType");
        List<ExceptionCausesEnum> tmp = ExceptionCausesEnum.getExceptionCauses(createUserType);
        List<InitComboxVO> list = new ArrayList<>();
        for (ExceptionCausesEnum exceptionCausesEnum : tmp) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(exceptionCausesEnum.getCode().longValue());
            initComboxVO.setName(exceptionCausesEnum.getDesc());
            list.add(initComboxVO);
        }

        return CommonResult.success(list);
    }

    @ApiOperation(value = "异常反馈")
    @PostMapping(value = "/exceptionFeedback")
    public CommonResult exceptionFeedback(@RequestBody @Valid AddAirExceptionFeedbackForm form) {
        this.airOrderService.exceptionFeedback(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "查看接口文件 airOrderId=空运订单id")
    @PostMapping(value = "/viewInterfaceFile")
    public CommonResult viewInterfaceFile(@RequestBody Map<String, Object> map) {
        Long airOrderId = MapUtil.getLong(map, "airOrderId");
        if (airOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Object url = this.fileClient.getBaseUrl().getData();

        List<AirExtensionField> airExtensionFields = this.airExtensionFieldService.getByCondition(new AirExtensionField().setBusinessId(airOrderId)
                .setType(ExtensionFieldTypeEnum.TWO.getCode()));
        List<Object> list = new ArrayList<>();
        for (AirExtensionField airExtensionField : airExtensionFields) {
            if (StringUtils.isEmpty(airExtensionField.getValue())) {
                continue;
            }
            JSONObject fileObj = new JSONObject(airExtensionField.getValue());
            JSONObject fileView = fileObj.getJSONObject("fileView");
            fileView.set("absolutePath", url + fileView.getStr("relativePath"));
            list.add(fileView);
        }
        return CommonResult.success(list);
    }

    @ApiOperation(value = "下拉空运订单状态")
    @PostMapping(value = "/initAirStatus")
    public CommonResult<List<InitComboxStrVO>> initAirStatus() {
        OrderStatusEnum[] statusEnums = {AIR_A_0,
                AIR_A_1, AIR_A_1_1, AIR_A_2, AIR_A_2_1, AIR_A_3, AIR_A_3_1,
                AIR_A_3_2, AIR_A_4, AIR_A_5, AIR_A_6, AIR_A_7, AIR_A_8};
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (OrderStatusEnum statusEnum : statusEnums) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(statusEnum.getDesc());
            initComboxStrVO.setCode(statusEnum.getCode());
            initComboxStrVOS.add(initComboxStrVO);
        }
        return CommonResult.success(initComboxStrVOS);
    }


}

