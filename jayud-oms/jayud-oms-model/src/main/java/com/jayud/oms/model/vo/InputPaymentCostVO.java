package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class InputPaymentCostVO {

    @ApiModelProperty(value = "应付主键ID,修改时传")
    private Long id;

    @ApiModelProperty(value = "应付客户名称")
    private String customerName;

    @ApiModelProperty(value = "应付项目CODE")
    private String costCode;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "数量")
    private Integer number;

    @ApiModelProperty(value = "币种CODE")
    private String currencyCode;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "本币金额")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
