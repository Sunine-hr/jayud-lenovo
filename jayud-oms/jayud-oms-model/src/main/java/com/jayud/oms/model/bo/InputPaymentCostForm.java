package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;


@Data
public class InputPaymentCostForm {

    @ApiModelProperty(value = "应付主键ID,修改时传")
    private Long id;

    @ApiModelProperty(value = "应付客户名称用户自填",required = true)
    @NotEmpty(message = "customerName is required")
    private String customerName;

    @ApiModelProperty(value = "应付项目CODE",required = true)
    @NotEmpty(message = "costCode is required")
    private String costCode;

    @ApiModelProperty(value = "应付项目",required = true)
    @NotEmpty(message = "costName is required")
    private String costName;

    @ApiModelProperty(value = "单价",required = true)
    @NotEmpty(message = "unitPrice is required")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "数量",required = true)
    @NotEmpty(message = "number is required")
    private Integer number;

    @ApiModelProperty(value = "币种CODE",required = true)
    @NotEmpty(message = "currencyCode is required")
    private String currencyCode;

    @ApiModelProperty(value = "币种",required = true)
    @NotEmpty(message = "currencyName is required")
    private String currencyName;

    @ApiModelProperty(value = "应付金额",required = true)
    @NotEmpty(message = "amount is required")
    private BigDecimal amount;

    @ApiModelProperty(value = "汇率",required = true)
    @NotEmpty(message = "exchangeRate is required")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "本币金额",required = true)
    @NotEmpty(message = "changeAmount is required")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
