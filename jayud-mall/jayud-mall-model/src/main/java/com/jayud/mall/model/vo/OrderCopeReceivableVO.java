package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCopeReceivableVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "订单ID(order_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Long orderId;

    @ApiModelProperty(value = "费用代码(cost_item cost_code)", position = 3)
    @JSONField(ordinal = 3)
    private String costCode;

    @ApiModelProperty(value = "费用名称(cost_item cost_name)", position = 4)
    @JSONField(ordinal = 4)
    private String costName;

    @ApiModelProperty(value = "计算方式(1自动 2手动)")
    private Integer calculateWay;

    @ApiModelProperty(value = "数量")
    private BigDecimal count;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "金额", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer cid;

    @ApiModelProperty(value = "描述", position = 7)
    @JSONField(ordinal = 7)
    private String remarks;

    /*币种：currency_info*/
    @ApiModelProperty(value = "币种代码", position = 8)
    @JSONField(ordinal = 8)
    private String currencyCode;

    @ApiModelProperty(value = "币种名称", position = 9)
    @JSONField(ordinal = 9)
    private String currencyName;

    //提单id
    @ApiModelProperty(value = "提单id(ocean_bill id)", position = 10)
    @JSONField(ordinal = 10)
    private Long billId;

    //客户id
    @ApiModelProperty(value = "客户ID(customer id)", position = 11)
    @JSONField(ordinal = 11)
    private Integer customerId;

    //客户名称 company
    @ApiModelProperty(value = "客户名称(customer company)", position = 12)
    @JSONField(ordinal = 12)
    private String customerName;

    @ApiModelProperty(value = "状态(0未生成账单 1已生成账单)", position = 13)
    @JSONField(ordinal = 13)
    private Integer status;

}
