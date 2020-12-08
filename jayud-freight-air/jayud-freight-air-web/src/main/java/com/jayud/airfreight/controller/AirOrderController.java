package com.jayud.airfreight.controller;


import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.model.bo.AirProcessOptForm;
import com.jayud.airfreight.model.bo.QueryAirOrderForm;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.model.po.Goods;
import com.jayud.airfreight.model.vo.AirOrderFormVO;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.airfreight.service.IGoodsService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.VivoApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/airOrder")
public class AirOrderController {
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private IAirOrderService airOrderService;
    @Autowired
    private IGoodsService goodsService;

    @ApiOperation(value = "分页查询空运订单")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryAirOrderForm form) {

        //模糊查询客户信息
        if (form.getCustomerName() != null) {
            ApiResult result = omsClient.getByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null) {
                JSONArray mainOrders = JSONArray.parseArray(JSON.toJSONString(data));
                form.assemblyMainOrderNo(mainOrders);
            }
        }

        //获取下个节点状态
        OrderStatusEnum statusEnum = OrderStatusEnum.getAirOrderPreStatus(form.getStatus());

        form.setStatus(statusEnum == null ? null : statusEnum.getCode());

        IPage<AirOrderFormVO> page = this.airOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success(new CommonPageResult<>(page));
        }

        List<AirOrderFormVO> records = page.getRecords();
        List<Long> airOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        for (AirOrderFormVO record : records) {
            airOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
        }
        //查询商品信息
        List<Goods> goods = goodsService.getGoodsByBusIds(airOrderIds, BusinessTypeEnum.KY.getDesc());
        //查询客户信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (AirOrderFormVO record : records) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
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
        if (airOrder.getProcessStatus() == 1) {
            return CommonResult.error(400, "该订单已经完成");
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
                this.airOrderService.updateProcessStatus(new AirOrder(), form);
                break;
            case AIR_A_2: //订舱
                this.airOrderService.doAirBookingOpt(form);
                break;
            case AIR_A_3: //订单入仓
                //是否能入仓
                if (!this.airOrderService.isWarehousing(airOrder)) {
                    return CommonResult.error(400, "订舱待确认,无法入仓");
                }
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

        return CommonResult.success();
    }


    @ApiOperation(value = "接单驳回")
    @PostMapping(value = "/receivingOrdersReject")
    public CommonResult receivingOrdersReject(@RequestBody AirProcessOptForm form) {
        //接单驳回

        //根据id批量删除物流轨迹表

        //修改订单驳回状态

        //主订单可以进行编辑

        //编辑完状态更改为待接单状态

        return null;
    }

    @ApiOperation(value = "订舱驳回 id=空运订单id")
    @PostMapping(value = "/bookingRejected")
    public CommonResult bookingRejected(@RequestBody Map<String, Object> form) {
        Long airOrderId = MapUtil.getLong(form, "id");
        if (airOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }

        AirOrder airOrder = this.airOrderService.getById(airOrderId);
        if (!OrderStatusEnum.AIR_A_2.getCode().equals(airOrder.getStatus())) {
            log.warn("当前订单状态无法进行操作 status={}", OrderStatusEnum.getDesc(airOrder.getStatus()));
            return CommonResult.error(400, "当前订单状态无法进行操作");
        }
        //修改空运订单状态为订舱驳回状态
        this.airOrderService.bookingRejected(airOrder);
        return CommonResult.success();
    }


    @ApiOperation(value = "订舱驳回编辑 id=空运订单id")
    @PostMapping(value = "/bookingRejectionEdit")
    public CommonResult bookingRejectionEdit(@RequestBody  AirProcessOptForm form) {

        form.setStatus(OrderStatusEnum.AIR_A_2.getCode());

        return null;
    }
}

