package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;


@Data
public class InputReceivableCostForm {

    @ApiModelProperty(value = "应收主键ID,修改时传")
    private Long id;

    @ApiModelProperty(value = "客户名称CODE",required = true)
    @NotEmpty(message = "customerCode is required")
    private String customerCode;

    @ApiModelProperty(value = "客户名称",required = true)
    @NotEmpty(message = "customerName is required")
    private String customerName;

    @ApiModelProperty(value = "应收项目CODE",required = true)
    @NotEmpty(message = "costCode is required")
    private String costCode;

    @ApiModelProperty(value = "费用类型ID,后面去掉了费用类型",required = true)
    @NotEmpty(message = "costTypeId is required")
    private Long costTypeId;

    @ApiModelProperty(value = "单价",required = true)
    @NotEmpty(message = "unitPrice is required")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "数量",required = true)
    @NotEmpty(message = "number is required")
    private Integer number;

    @ApiModelProperty(value = "币种CODE",required = true)
    @NotEmpty(message = "currencyCode is required")
    private String currencyCode;

    @ApiModelProperty(value = "应收金额",required = true)
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

    @ApiModelProperty(value = "状态")
    private String status;

}
