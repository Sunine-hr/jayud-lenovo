package com.jayud.finance.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderReceiveBillForm {

    @ApiModelProperty(value = "法人主体",required = true)
    @NotEmpty(message = "legalName is required")
    private String legalName;

    @ApiModelProperty(value = "供应商",required = true)
    @NotEmpty(message = "supplierChName is required")
    private String supplierChName;

    @ApiModelProperty(value = "已出账金额(人民币)",required = true)
    @NotNull(message = "alreadyPaidAmount is required")
    private BigDecimal alreadyPaidAmount;

    @ApiModelProperty(value = "已出账订单数",required = true)
    @NotNull(message = "billOrderNum is required")
    private Integer billOrderNum;

    @ApiModelProperty(value = "账单数",required = true)
    @NotNull(message = "billNum is required")
    private Integer billNum;
}
