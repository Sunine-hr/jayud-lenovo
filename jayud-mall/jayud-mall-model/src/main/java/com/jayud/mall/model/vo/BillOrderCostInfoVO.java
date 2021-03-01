package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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
    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer supplierId;

    @ApiModelProperty(value = "【箱数】提单对应的订单箱数，不是订单真正的箱数", position = 7)
    @JSONField(ordinal = 7)
    private BigDecimal quantity;

    @ApiModelProperty(value = "【供应商】提单对应的供应商，订单没有供应商", position = 8)
    @JSONField(ordinal = 8)
    private String supplierName;

    //提单的费用 分摊 订单

    //提单费用产生的订单应收
    @ApiModelProperty(value = "提单费用产生的订单应收", position = 9)
    @JSONField(ordinal = 9)
    private List<OrderCopeReceivableVO> orderCopeReceivableVOS;
    //提单费用产生的订单应付
    @ApiModelProperty(value = "提单费用产生的订单应付", position = 10)
    @JSONField(ordinal = 10)
    private List<OrderCopeWithVO> orderCopeWithVOS;



}