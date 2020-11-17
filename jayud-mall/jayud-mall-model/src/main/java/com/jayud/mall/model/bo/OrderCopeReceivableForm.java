package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCopeReceivableForm {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "订单ID(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)")
    private Integer cid;

    @ApiModelProperty(value = "描述")
    private String remarks;
}
