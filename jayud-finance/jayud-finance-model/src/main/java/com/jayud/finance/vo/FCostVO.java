package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 开票/付款一致审核列表
 */
@Data
public class FCostVO {

    @ApiModelProperty(value = "客户名(customer_info name)")
    private String customerName;

    @ApiModelProperty(value = "费用类型名称")
    private String costGenreName;

    @ApiModelProperty(value = "收费项目")
    private String costName;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "数量")
    private Integer number;

    @ApiModelProperty(value = "币种")
    private String currencyName;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "本币金额")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
