package com.jayud.Inlandtransport.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.Inlandtransport.feign.OauthClient;
import com.jayud.Inlandtransport.feign.OmsClient;
import com.jayud.Inlandtransport.model.bo.ProcessOptForm;
import com.jayud.Inlandtransport.model.bo.QueryOrderForm;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.GoodsVO;
import com.jayud.Inlandtransport.model.vo.OrderAddressVO;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportFormVO;
import com.jayud.Inlandtransport.model.vo.SendCarPdfVO;
import com.jayud.Inlandtransport.service.IOrderInlandSendCarsService;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.aop.annotations.DynamicHead;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

        IPage<OrderInlandTransportFormVO> page = this.orderInlandTransportService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success(new CommonPageResult<>(page));
        }

        List<OrderInlandTransportFormVO> records = page.getRecords();
        List<Long> orderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<Long> supplierIds = new ArrayList<>();
        for (OrderInlandTransportFormVO record : records) {
            orderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            if (record.getSupplierId() != null) {
                supplierIds.add(record.getSupplierId());
            }

        }
        //查询商品信息
        List<GoodsVO> goods = this.omsClient.getGoodsByBusIds(orderIds, BusinessTypeEnum.NL.getCode()).getData();
        //查询订单地址
        List<OrderAddressVO> orderAddressList = this.omsClient.getOrderAddressByBusIds(orderIds, BusinessTypeEnum.NL.getCode()).getData();
        //查询法人主体
//        ApiResult legalEntityResult = null;
//        if (CollectionUtils.isNotEmpty(entityIds)) {
//            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
//        }
        //查询供应商信息
        JSONArray supplierInfo = null;
        if (CollectionUtils.isNotEmpty(supplierIds)) {
            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
        }
        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (OrderInlandTransportFormVO record : records) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            //拼装地址信息
            record.assemblyAddressInfo(orderAddressList);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
//            record.assemblyLegalEntity(legalEntityResult);
            //拼装供应商
            record.assemblySupplierInfo(supplierInfo);
        }
        return CommonResult.success(new CommonPageResult(page));
    }


    @ApiOperation(value = "执行程操作")
    @PostMapping(value = "/doProcessOpt")
    public CommonResult doProcessOpt(@RequestBody ProcessOptForm form) {
        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("空运订单编号/空运订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //空运订单信息
        OrderInlandTransport order = this.orderInlandTransportService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(order.getProcessStatus())) {
            return CommonResult.error(400, "该订单已经完成");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(order.getProcessStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }
        //查询下一步节点
        String nextStatus = omsClient.getOrderProcessNode(order.getMainOrderNo()
                , order.getOrderNo(), order.getStatus()).getData();
        if (nextStatus == null) {
            log.error("执行订单流程操作失败,超出流程之外 data={}", order.toString());
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
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


    @ApiOperation(value = "渲染数据,确认派车 orderNo=订单号(主/子),entranceType=入口类型(1主订单,2子订单)")
    @PostMapping(value = "/initPdfData")
    public CommonResult<SendCarPdfVO> initPdfData(@RequestBody Map<String, Object> param) {
        String orderNo = MapUtil.getStr(param, CommonConstant.ORDER_NO);
        Integer entranceType = MapUtil.getInt(param, "entranceType");
        if (StringUtil.isNullOrEmpty(orderNo) && entranceType == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OrderInlandTransport subOrder = null;
        if (entranceType == 1) {
            List<OrderInlandTransport> subOrders = this.orderInlandTransportService.getByCondition(
                    new OrderInlandTransport().setMainOrderNo(orderNo));
            if (CollectionUtil.isEmpty(subOrders)) {
                return CommonResult.error(400, "不存在该订单信息");
            } else {
                subOrder = subOrders.get(0);
            }
        } else {
            subOrder = this.orderInlandTransportService.getByCondition(new OrderInlandTransport().setOrderNo(orderNo)).get(0);
        }
        SendCarPdfVO sendCarPdfVO = orderInlandSendCarsService.initPdfData(subOrder, CommonConstant.NLYS_DESC);
        return CommonResult.success(sendCarPdfVO);
    }
}

