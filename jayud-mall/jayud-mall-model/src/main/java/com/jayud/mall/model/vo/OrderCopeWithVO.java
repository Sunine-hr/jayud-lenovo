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

    @ApiModelProperty(value = "费用代码(cost_item cost_code)")
    private String costCode;

    @ApiModelProperty(value = "费用名称(cost_item cost_name)")
    private String costName;

    @ApiModelProperty(value = "供应商id(supplier_info id)")
    private Integer supplierId;

    @ApiModelProperty(value = "计算方式(1自动 2手动)")
    private Integer calculateWay;

    @ApiModelProperty(value = "数量")
    private BigDecimal count;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)")
    private Integer cid;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Long billId;

    @ApiModelProperty(value = "状态(0未生成账单 1已生成账单)")
    private Integer status;

    @ApiModelProperty(value = "订单服务对应应付费用id(order_service_with id)")
    private Long orderServiceWithId;

    /*扩展信息*/

    @ApiModelProperty(value = "供应商名称(公司名称 supplier_info.company_name)")
    private String supplierName;

    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "币种名称")
    private String currencyName;




}
