package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCopeWithVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "订单ID(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "供应商代码(supplier_info supplier_code)")
    private String supplierCode;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)")
    private Integer cid;

    @ApiModelProperty(value = "描述")
    private String remarks;

    /*供应商信息:supplier_info*/
    @ApiModelProperty(value = "供应商名称(中)")
    private String supplierChName;

    /*币种:currency_info*/
    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "币种名称")
    private String currencyName;



}
