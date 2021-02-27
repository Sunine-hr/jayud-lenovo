package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(value = "BillOrderCostInfoVO", description = "提单对应订单费用信息")
@Data
public class BillOrderCostInfoVO {

    //订单
    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    @JSONField(ordinal = 2)
    private String orderNo;

    @ApiModelProperty(value = "【重量】实际重量(KG)", position = 3)
    @JSONField(ordinal = 3)
    private BigDecimal actualWeight;

    @ApiModelProperty(value = "【体积m3】实际体积(m3)", position = 4)
    @JSONField(ordinal = 4)
    private BigDecimal actualVolume;

    @ApiModelProperty(value = "【计费重】收费重(KG)", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal chargeWeight;

    //提单
    @ApiModelProperty(value = "【计费重】收费重(KG)", position = 6)
    @JSONField(ordinal = 6)
    private Integer supplierId;

    @ApiModelProperty(value = "【箱数】提单对应的订单箱数，不是订单真正的箱数", position = 7)
    @JSONField(ordinal = 7)
    private BigDecimal quantity;

    @ApiModelProperty(value = "【供应商】提单对应的供应商，订单没有供应商", position = 8)
    @JSONField(ordinal = 8)
    private String supplierName;

    //订单对应的提单费用
    //TODO 待实现...,需要修改订单应收应付费用的表结构
    //应付应收都要有



}
